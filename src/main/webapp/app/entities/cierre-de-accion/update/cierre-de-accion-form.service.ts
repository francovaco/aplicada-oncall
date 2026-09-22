import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICierreDeAccion, NewCierreDeAccion } from '../cierre-de-accion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICierreDeAccion for edit and NewCierreDeAccionFormGroupInput for create.
 */
type CierreDeAccionFormGroupInput = ICierreDeAccion | PartialWithRequiredKeyOf<NewCierreDeAccion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICierreDeAccion | NewCierreDeAccion> = Omit<T, 'cerradoEn'> & {
  cerradoEn?: string | null;
};

type CierreDeAccionFormRawValue = FormValueOf<ICierreDeAccion>;

type NewCierreDeAccionFormRawValue = FormValueOf<NewCierreDeAccion>;

type CierreDeAccionFormDefaults = Pick<NewCierreDeAccion, 'id' | 'cerradoEn'>;

type CierreDeAccionFormGroupContent = {
  id: FormControl<CierreDeAccionFormRawValue['id'] | NewCierreDeAccion['id']>;
  comentarioCierre: FormControl<CierreDeAccionFormRawValue['comentarioCierre']>;
  cerradoEn: FormControl<CierreDeAccionFormRawValue['cerradoEn']>;
  asignacion: FormControl<CierreDeAccionFormRawValue['asignacion']>;
  cerradoPor: FormControl<CierreDeAccionFormRawValue['cerradoPor']>;
};

export type CierreDeAccionFormGroup = FormGroup<CierreDeAccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CierreDeAccionFormService {
  createCierreDeAccionFormGroup(cierreDeAccion?: CierreDeAccionFormGroupInput): CierreDeAccionFormGroup {
    const cierreDeAccionRawValue = this.convertCierreDeAccionToCierreDeAccionRawValue({
      ...this.getFormDefaults(),
      ...(cierreDeAccion ?? { id: null }),
    });

    return new FormGroup<CierreDeAccionFormGroupContent>({
      id: new FormControl(
        { value: cierreDeAccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      comentarioCierre: new FormControl(cierreDeAccionRawValue.comentarioCierre, {
        validators: [Validators.maxLength(2000)],
      }),
      cerradoEn: new FormControl(cierreDeAccionRawValue.cerradoEn, {
        validators: [Validators.required],
      }),
      asignacion: new FormControl(cierreDeAccionRawValue.asignacion, {
        validators: [Validators.required],
      }),
      cerradoPor: new FormControl(cierreDeAccionRawValue.cerradoPor, {
        validators: [Validators.required],
      }),
    });
  }

  getCierreDeAccion(form: CierreDeAccionFormGroup): ICierreDeAccion | NewCierreDeAccion {
    return this.convertCierreDeAccionRawValueToCierreDeAccion(form.getRawValue());
  }

  resetForm(form: CierreDeAccionFormGroup, cierreDeAccion: CierreDeAccionFormGroupInput): void {
    const cierreDeAccionRawValue = this.convertCierreDeAccionToCierreDeAccionRawValue({ ...this.getFormDefaults(), ...cierreDeAccion });
    form.reset({
      ...cierreDeAccionRawValue,
      id: { value: cierreDeAccionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CierreDeAccionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      cerradoEn: currentTime,
    };
  }

  private convertCierreDeAccionRawValueToCierreDeAccion(
    rawCierreDeAccion: CierreDeAccionFormRawValue | NewCierreDeAccionFormRawValue,
  ): ICierreDeAccion | NewCierreDeAccion {
    return {
      ...rawCierreDeAccion,
      cerradoEn: dayjs(rawCierreDeAccion.cerradoEn, DATE_TIME_FORMAT),
    };
  }

  private convertCierreDeAccionToCierreDeAccionRawValue(
    cierreDeAccion: ICierreDeAccion | (Partial<NewCierreDeAccion> & CierreDeAccionFormDefaults),
  ): CierreDeAccionFormRawValue | PartialWithRequiredKeyOf<NewCierreDeAccionFormRawValue> {
    return {
      ...cierreDeAccion,
      cerradoEn: cierreDeAccion.cerradoEn ? cierreDeAccion.cerradoEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
