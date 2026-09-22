import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAsignacionDeAccion, NewAsignacionDeAccion } from '../asignacion-de-accion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAsignacionDeAccion for edit and NewAsignacionDeAccionFormGroupInput for create.
 */
type AsignacionDeAccionFormGroupInput = IAsignacionDeAccion | PartialWithRequiredKeyOf<NewAsignacionDeAccion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAsignacionDeAccion | NewAsignacionDeAccion> = Omit<T, 'creadaEn'> & {
  creadaEn?: string | null;
};

type AsignacionDeAccionFormRawValue = FormValueOf<IAsignacionDeAccion>;

type NewAsignacionDeAccionFormRawValue = FormValueOf<NewAsignacionDeAccion>;

type AsignacionDeAccionFormDefaults = Pick<NewAsignacionDeAccion, 'id' | 'creadaEn'>;

type AsignacionDeAccionFormGroupContent = {
  id: FormControl<AsignacionDeAccionFormRawValue['id'] | NewAsignacionDeAccion['id']>;
  titulo: FormControl<AsignacionDeAccionFormRawValue['titulo']>;
  descripcion: FormControl<AsignacionDeAccionFormRawValue['descripcion']>;
  fechaVencimiento: FormControl<AsignacionDeAccionFormRawValue['fechaVencimiento']>;
  estado: FormControl<AsignacionDeAccionFormRawValue['estado']>;
  creadaEn: FormControl<AsignacionDeAccionFormRawValue['creadaEn']>;
  accionCorrectiva: FormControl<AsignacionDeAccionFormRawValue['accionCorrectiva']>;
  responsable: FormControl<AsignacionDeAccionFormRawValue['responsable']>;
};

export type AsignacionDeAccionFormGroup = FormGroup<AsignacionDeAccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AsignacionDeAccionFormService {
  createAsignacionDeAccionFormGroup(asignacionDeAccion?: AsignacionDeAccionFormGroupInput): AsignacionDeAccionFormGroup {
    const asignacionDeAccionRawValue = this.convertAsignacionDeAccionToAsignacionDeAccionRawValue({
      ...this.getFormDefaults(),
      ...(asignacionDeAccion ?? { id: null }),
    });

    return new FormGroup<AsignacionDeAccionFormGroupContent>({
      id: new FormControl(
        { value: asignacionDeAccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titulo: new FormControl(asignacionDeAccionRawValue.titulo, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      descripcion: new FormControl(asignacionDeAccionRawValue.descripcion, {
        validators: [Validators.maxLength(2000)],
      }),
      fechaVencimiento: new FormControl(asignacionDeAccionRawValue.fechaVencimiento, {
        validators: [Validators.required],
      }),
      estado: new FormControl(asignacionDeAccionRawValue.estado, {
        validators: [Validators.required],
      }),
      creadaEn: new FormControl(asignacionDeAccionRawValue.creadaEn, {
        validators: [Validators.required],
      }),
      accionCorrectiva: new FormControl(asignacionDeAccionRawValue.accionCorrectiva, {
        validators: [Validators.required],
      }),
      responsable: new FormControl(asignacionDeAccionRawValue.responsable),
    });
  }

  getAsignacionDeAccion(form: AsignacionDeAccionFormGroup): IAsignacionDeAccion | NewAsignacionDeAccion {
    return this.convertAsignacionDeAccionRawValueToAsignacionDeAccion(form.getRawValue());
  }

  resetForm(form: AsignacionDeAccionFormGroup, asignacionDeAccion: AsignacionDeAccionFormGroupInput): void {
    const asignacionDeAccionRawValue = this.convertAsignacionDeAccionToAsignacionDeAccionRawValue({
      ...this.getFormDefaults(),
      ...asignacionDeAccion,
    });
    form.reset({
      ...asignacionDeAccionRawValue,
      id: { value: asignacionDeAccionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AsignacionDeAccionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creadaEn: currentTime,
    };
  }

  private convertAsignacionDeAccionRawValueToAsignacionDeAccion(
    rawAsignacionDeAccion: AsignacionDeAccionFormRawValue | NewAsignacionDeAccionFormRawValue,
  ): IAsignacionDeAccion | NewAsignacionDeAccion {
    return {
      ...rawAsignacionDeAccion,
      creadaEn: dayjs(rawAsignacionDeAccion.creadaEn, DATE_TIME_FORMAT),
    };
  }

  private convertAsignacionDeAccionToAsignacionDeAccionRawValue(
    asignacionDeAccion: IAsignacionDeAccion | (Partial<NewAsignacionDeAccion> & AsignacionDeAccionFormDefaults),
  ): AsignacionDeAccionFormRawValue | PartialWithRequiredKeyOf<NewAsignacionDeAccionFormRawValue> {
    return {
      ...asignacionDeAccion,
      creadaEn: asignacionDeAccion.creadaEn ? asignacionDeAccion.creadaEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
