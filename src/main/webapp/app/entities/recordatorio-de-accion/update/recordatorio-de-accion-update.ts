import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IAsignacionDeAccion } from 'app/entities/asignacion-de-accion/asignacion-de-accion.model';
import { AsignacionDeAccionService } from 'app/entities/asignacion-de-accion/service/asignacion-de-accion.service';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IRecordatorioDeAccion } from '../recordatorio-de-accion.model';
import { RecordatorioDeAccionService } from '../service/recordatorio-de-accion.service';

import { RecordatorioDeAccionFormGroup, RecordatorioDeAccionFormService } from './recordatorio-de-accion-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-recordatorio-de-accion-update',
  templateUrl: './recordatorio-de-accion-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class RecordatorioDeAccionUpdate implements OnInit {
  readonly isSaving = signal(false);
  recordatorioDeAccion: IRecordatorioDeAccion | null = null;

  asignacionDeAccionsSharedCollection = signal<IAsignacionDeAccion[]>([]);

  protected recordatorioDeAccionService = inject(RecordatorioDeAccionService);
  protected recordatorioDeAccionFormService = inject(RecordatorioDeAccionFormService);
  protected asignacionDeAccionService = inject(AsignacionDeAccionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecordatorioDeAccionFormGroup = this.recordatorioDeAccionFormService.createRecordatorioDeAccionFormGroup();

  compareAsignacionDeAccion = (o1: IAsignacionDeAccion | null, o2: IAsignacionDeAccion | null): boolean =>
    this.asignacionDeAccionService.compareAsignacionDeAccion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recordatorioDeAccion }) => {
      this.recordatorioDeAccion = recordatorioDeAccion;
      if (recordatorioDeAccion) {
        this.updateForm(recordatorioDeAccion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const recordatorioDeAccion = this.recordatorioDeAccionFormService.getRecordatorioDeAccion(this.editForm);
    if (recordatorioDeAccion.id === null) {
      this.subscribeToSaveResponse(this.recordatorioDeAccionService.create(recordatorioDeAccion));
    } else {
      this.subscribeToSaveResponse(this.recordatorioDeAccionService.update(recordatorioDeAccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRecordatorioDeAccion | null>): void {
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

  protected updateForm(recordatorioDeAccion: IRecordatorioDeAccion): void {
    this.recordatorioDeAccion = recordatorioDeAccion;
    this.recordatorioDeAccionFormService.resetForm(this.editForm, recordatorioDeAccion);

    this.asignacionDeAccionsSharedCollection.update(asignacionDeAccions =>
      this.asignacionDeAccionService.addAsignacionDeAccionToCollectionIfMissing<IAsignacionDeAccion>(
        asignacionDeAccions,
        recordatorioDeAccion.asignacion,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.asignacionDeAccionService
      .query()
      .pipe(map((res: HttpResponse<IAsignacionDeAccion[]>) => res.body ?? []))
      .pipe(
        map((asignacionDeAccions: IAsignacionDeAccion[]) =>
          this.asignacionDeAccionService.addAsignacionDeAccionToCollectionIfMissing<IAsignacionDeAccion>(
            asignacionDeAccions,
            this.recordatorioDeAccion?.asignacion,
          ),
        ),
      )
      .subscribe((asignacionDeAccions: IAsignacionDeAccion[]) => this.asignacionDeAccionsSharedCollection.set(asignacionDeAccions));
  }
}
