import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecordatorioDeAccion, NewRecordatorioDeAccion } from '../recordatorio-de-accion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecordatorioDeAccion for edit and NewRecordatorioDeAccionFormGroupInput for create.
 */
type RecordatorioDeAccionFormGroupInput = IRecordatorioDeAccion | PartialWithRequiredKeyOf<NewRecordatorioDeAccion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRecordatorioDeAccion | NewRecordatorioDeAccion> = Omit<T, 'enviadoEn'> & {
  enviadoEn?: string | null;
};

type RecordatorioDeAccionFormRawValue = FormValueOf<IRecordatorioDeAccion>;

type NewRecordatorioDeAccionFormRawValue = FormValueOf<NewRecordatorioDeAccion>;

type RecordatorioDeAccionFormDefaults = Pick<NewRecordatorioDeAccion, 'id' | 'enviadoEn'>;

type RecordatorioDeAccionFormGroupContent = {
  id: FormControl<RecordatorioDeAccionFormRawValue['id'] | NewRecordatorioDeAccion['id']>;
  mensaje: FormControl<RecordatorioDeAccionFormRawValue['mensaje']>;
  enviadoEn: FormControl<RecordatorioDeAccionFormRawValue['enviadoEn']>;
  nivelEscalamiento: FormControl<RecordatorioDeAccionFormRawValue['nivelEscalamiento']>;
  asignacion: FormControl<RecordatorioDeAccionFormRawValue['asignacion']>;
};

export type RecordatorioDeAccionFormGroup = FormGroup<RecordatorioDeAccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecordatorioDeAccionFormService {
  createRecordatorioDeAccionFormGroup(recordatorioDeAccion?: RecordatorioDeAccionFormGroupInput): RecordatorioDeAccionFormGroup {
    const recordatorioDeAccionRawValue = this.convertRecordatorioDeAccionToRecordatorioDeAccionRawValue({
      ...this.getFormDefaults(),
      ...(recordatorioDeAccion ?? { id: null }),
    });

    return new FormGroup<RecordatorioDeAccionFormGroupContent>({
      id: new FormControl(
        { value: recordatorioDeAccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mensaje: new FormControl(recordatorioDeAccionRawValue.mensaje, {
        validators: [Validators.required, Validators.maxLength(500)],
      }),
      enviadoEn: new FormControl(recordatorioDeAccionRawValue.enviadoEn),
      nivelEscalamiento: new FormControl(recordatorioDeAccionRawValue.nivelEscalamiento, {
        validators: [Validators.required, Validators.min(1), Validators.max(2)],
      }),
      asignacion: new FormControl(recordatorioDeAccionRawValue.asignacion, {
        validators: [Validators.required],
      }),
    });
  }

  getRecordatorioDeAccion(form: RecordatorioDeAccionFormGroup): IRecordatorioDeAccion | NewRecordatorioDeAccion {
    return this.convertRecordatorioDeAccionRawValueToRecordatorioDeAccion(form.getRawValue());
  }

  resetForm(form: RecordatorioDeAccionFormGroup, recordatorioDeAccion: RecordatorioDeAccionFormGroupInput): void {
    const recordatorioDeAccionRawValue = this.convertRecordatorioDeAccionToRecordatorioDeAccionRawValue({
      ...this.getFormDefaults(),
      ...recordatorioDeAccion,
    });
    form.reset({
      ...recordatorioDeAccionRawValue,
      id: { value: recordatorioDeAccionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): RecordatorioDeAccionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      enviadoEn: currentTime,
    };
  }

  private convertRecordatorioDeAccionRawValueToRecordatorioDeAccion(
    rawRecordatorioDeAccion: RecordatorioDeAccionFormRawValue | NewRecordatorioDeAccionFormRawValue,
  ): IRecordatorioDeAccion | NewRecordatorioDeAccion {
    return {
      ...rawRecordatorioDeAccion,
      enviadoEn: dayjs(rawRecordatorioDeAccion.enviadoEn, DATE_TIME_FORMAT),
    };
  }

  private convertRecordatorioDeAccionToRecordatorioDeAccionRawValue(
    recordatorioDeAccion: IRecordatorioDeAccion | (Partial<NewRecordatorioDeAccion> & RecordatorioDeAccionFormDefaults),
  ): RecordatorioDeAccionFormRawValue | PartialWithRequiredKeyOf<NewRecordatorioDeAccionFormRawValue> {
    return {
      ...recordatorioDeAccion,
      enviadoEn: recordatorioDeAccion.enviadoEn ? recordatorioDeAccion.enviadoEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
