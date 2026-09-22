import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IRecordatorioDeAccion } from '../recordatorio-de-accion.model';
import { RecordatorioDeAccionService } from '../service/recordatorio-de-accion.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './recordatorio-de-accion-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class RecordatorioDeAccionDeleteDialog {
  recordatorioDeAccion?: IRecordatorioDeAccion;

  protected readonly recordatorioDeAccionService = inject(RecordatorioDeAccionService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recordatorioDeAccionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
