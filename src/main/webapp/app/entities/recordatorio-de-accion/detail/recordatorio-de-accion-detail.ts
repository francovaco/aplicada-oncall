import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IRecordatorioDeAccion } from '../recordatorio-de-accion.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-recordatorio-de-accion-detail',
  templateUrl: './recordatorio-de-accion-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, TranslatePipe, RouterLink, FormatMediumDatetimePipe],
})
export class RecordatorioDeAccionDetail {
  readonly recordatorioDeAccion = input<IRecordatorioDeAccion | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
