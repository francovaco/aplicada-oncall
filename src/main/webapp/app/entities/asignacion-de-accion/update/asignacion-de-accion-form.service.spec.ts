import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../asignacion-de-accion.test-samples';

import { AsignacionDeAccionFormService } from './asignacion-de-accion-form.service';

describe('AsignacionDeAccion Form Service', () => {
  let service: AsignacionDeAccionFormService;

  beforeEach(() => {
    service = TestBed.inject(AsignacionDeAccionFormService);
  });

  describe('Service methods', () => {
    describe('createAsignacionDeAccionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAsignacionDeAccionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descripcion: expect.any(Object),
            fechaVencimiento: expect.any(Object),
            estado: expect.any(Object),
            creadaEn: expect.any(Object),
            accionCorrectiva: expect.any(Object),
            responsable: expect.any(Object),
          }),
        );
      });

      it('passing IAsignacionDeAccion should create a new form with FormGroup', () => {
        const formGroup = service.createAsignacionDeAccionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descripcion: expect.any(Object),
            fechaVencimiento: expect.any(Object),
            estado: expect.any(Object),
            creadaEn: expect.any(Object),
            accionCorrectiva: expect.any(Object),
            responsable: expect.any(Object),
          }),
        );
      });
    });

    describe('getAsignacionDeAccion', () => {
      it('should return NewAsignacionDeAccion for default AsignacionDeAccion initial value', () => {
        const formGroup = service.createAsignacionDeAccionFormGroup(sampleWithNewData);

        const asignacionDeAccion = service.getAsignacionDeAccion(formGroup);

        expect(asignacionDeAccion).toMatchObject(sampleWithNewData);
      });

      it('should return NewAsignacionDeAccion for empty AsignacionDeAccion initial value', () => {
        const formGroup = service.createAsignacionDeAccionFormGroup();

        const asignacionDeAccion = service.getAsignacionDeAccion(formGroup);

        expect(asignacionDeAccion).toMatchObject({});
      });

      it('should return IAsignacionDeAccion', () => {
        const formGroup = service.createAsignacionDeAccionFormGroup(sampleWithRequiredData);

        const asignacionDeAccion = service.getAsignacionDeAccion(formGroup);

        expect(asignacionDeAccion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAsignacionDeAccion should not enable id FormControl', () => {
        const formGroup = service.createAsignacionDeAccionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAsignacionDeAccion should disable id FormControl', () => {
        const formGroup = service.createAsignacionDeAccionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
