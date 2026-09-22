import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IAsignacionDeAccion } from 'app/entities/asignacion-de-accion/asignacion-de-accion.model';
import { AsignacionDeAccionService } from 'app/entities/asignacion-de-accion/service/asignacion-de-accion.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICierreDeAccion } from '../cierre-de-accion.model';
import { CierreDeAccionService } from '../service/cierre-de-accion.service';

import { CierreDeAccionFormGroup, CierreDeAccionFormService } from './cierre-de-accion-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-cierre-de-accion-update',
  templateUrl: './cierre-de-accion-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CierreDeAccionUpdate implements OnInit {
  readonly isSaving = signal(false);
  cierreDeAccion: ICierreDeAccion | null = null;

  asignacionDeAccionsSharedCollection = signal<IAsignacionDeAccion[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected cierreDeAccionService = inject(CierreDeAccionService);
  protected cierreDeAccionFormService = inject(CierreDeAccionFormService);
  protected asignacionDeAccionService = inject(AsignacionDeAccionService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CierreDeAccionFormGroup = this.cierreDeAccionFormService.createCierreDeAccionFormGroup();

  compareAsignacionDeAccion = (o1: IAsignacionDeAccion | null, o2: IAsignacionDeAccion | null): boolean =>
    this.asignacionDeAccionService.compareAsignacionDeAccion(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cierreDeAccion }) => {
      this.cierreDeAccion = cierreDeAccion;
      if (cierreDeAccion) {
        this.updateForm(cierreDeAccion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const cierreDeAccion = this.cierreDeAccionFormService.getCierreDeAccion(this.editForm);
    if (cierreDeAccion.id === null) {
      this.subscribeToSaveResponse(this.cierreDeAccionService.create(cierreDeAccion));
    } else {
      this.subscribeToSaveResponse(this.cierreDeAccionService.update(cierreDeAccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICierreDeAccion | null>): void {
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

  protected updateForm(cierreDeAccion: ICierreDeAccion): void {
    this.cierreDeAccion = cierreDeAccion;
    this.cierreDeAccionFormService.resetForm(this.editForm, cierreDeAccion);

    this.asignacionDeAccionsSharedCollection.update(asignacionDeAccions =>
      this.asignacionDeAccionService.addAsignacionDeAccionToCollectionIfMissing<IAsignacionDeAccion>(
        asignacionDeAccions,
        cierreDeAccion.asignacion,
      ),
    );
    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, cierreDeAccion.cerradoPor));
  }

  protected loadRelationshipsOptions(): void {
    this.asignacionDeAccionService
      .query()
      .pipe(map((res: HttpResponse<IAsignacionDeAccion[]>) => res.body ?? []))
      .pipe(
        map((asignacionDeAccions: IAsignacionDeAccion[]) =>
          this.asignacionDeAccionService.addAsignacionDeAccionToCollectionIfMissing<IAsignacionDeAccion>(
            asignacionDeAccions,
            this.cierreDeAccion?.asignacion,
          ),
        ),
      )
      .subscribe((asignacionDeAccions: IAsignacionDeAccion[]) => this.asignacionDeAccionsSharedCollection.set(asignacionDeAccions));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.cierreDeAccion?.cerradoPor)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
