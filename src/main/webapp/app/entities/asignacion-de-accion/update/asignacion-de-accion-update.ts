import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IAccionCorrectiva } from 'app/entities/accion-correctiva/accion-correctiva.model';
import { AccionCorrectivaService } from 'app/entities/accion-correctiva/service/accion-correctiva.service';
import { EstadoAccion } from 'app/entities/enumerations/estado-accion.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { IAsignacionDeAccion } from '../asignacion-de-accion.model';
import { AsignacionDeAccionService } from '../service/asignacion-de-accion.service';

import { AsignacionDeAccionFormGroup, AsignacionDeAccionFormService } from './asignacion-de-accion-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-asignacion-de-accion-update',
  templateUrl: './asignacion-de-accion-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class AsignacionDeAccionUpdate implements OnInit {
  readonly isSaving = signal(false);
  asignacionDeAccion: IAsignacionDeAccion | null = null;
  estadoAccionValues = Object.keys(EstadoAccion);

  accionCorrectivasSharedCollection = signal<IAccionCorrectiva[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected asignacionDeAccionService = inject(AsignacionDeAccionService);
  protected asignacionDeAccionFormService = inject(AsignacionDeAccionFormService);
  protected accionCorrectivaService = inject(AccionCorrectivaService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AsignacionDeAccionFormGroup = this.asignacionDeAccionFormService.createAsignacionDeAccionFormGroup();

  compareAccionCorrectiva = (o1: IAccionCorrectiva | null, o2: IAccionCorrectiva | null): boolean =>
    this.accionCorrectivaService.compareAccionCorrectiva(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ asignacionDeAccion }) => {
      this.asignacionDeAccion = asignacionDeAccion;
      if (asignacionDeAccion) {
        this.updateForm(asignacionDeAccion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const asignacionDeAccion = this.asignacionDeAccionFormService.getAsignacionDeAccion(this.editForm);
    if (asignacionDeAccion.id === null) {
      this.subscribeToSaveResponse(this.asignacionDeAccionService.create(asignacionDeAccion));
    } else {
      this.subscribeToSaveResponse(this.asignacionDeAccionService.update(asignacionDeAccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAsignacionDeAccion | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(asignacionDeAccion: IAsignacionDeAccion): void {
    this.asignacionDeAccion = asignacionDeAccion;
    this.asignacionDeAccionFormService.resetForm(this.editForm, asignacionDeAccion);

    this.accionCorrectivasSharedCollection.update(accionCorrectivas =>
      this.accionCorrectivaService.addAccionCorrectivaToCollectionIfMissing<IAccionCorrectiva>(
        accionCorrectivas,
        asignacionDeAccion.accionCorrectiva,
      ),
    );
    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, asignacionDeAccion.responsable));
  }

  protected loadRelationshipsOptions(): void {
    this.accionCorrectivaService
      .query()
      .pipe(map((res: HttpResponse<IAccionCorrectiva[]>) => res.body ?? []))
      .pipe(
        map((accionCorrectivas: IAccionCorrectiva[]) =>
          this.accionCorrectivaService.addAccionCorrectivaToCollectionIfMissing<IAccionCorrectiva>(
            accionCorrectivas,
            this.asignacionDeAccion?.accionCorrectiva,
          ),
        ),
      )
      .subscribe((accionCorrectivas: IAccionCorrectiva[]) => this.accionCorrectivasSharedCollection.set(accionCorrectivas));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.asignacionDeAccion?.responsable)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
