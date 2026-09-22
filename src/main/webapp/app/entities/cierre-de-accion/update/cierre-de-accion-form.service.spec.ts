import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cierre-de-accion.test-samples';

import { CierreDeAccionFormService } from './cierre-de-accion-form.service';

describe('CierreDeAccion Form Service', () => {
  let service: CierreDeAccionFormService;

  beforeEach(() => {
    service = TestBed.inject(CierreDeAccionFormService);
  });

  describe('Service methods', () => {
    describe('createCierreDeAccionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCierreDeAccionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            comentarioCierre: expect.any(Object),
            cerradoEn: expect.any(Object),
            asignacion: expect.any(Object),
            cerradoPor: expect.any(Object),
          }),
        );
      });

      it('passing ICierreDeAccion should create a new form with FormGroup', () => {
        const formGroup = service.createCierreDeAccionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            comentarioCierre: expect.any(Object),
            cerradoEn: expect.any(Object),
            asignacion: expect.any(Object),
            cerradoPor: expect.any(Object),
          }),
        );
      });
    });

    describe('getCierreDeAccion', () => {
      it('should return NewCierreDeAccion for default CierreDeAccion initial value', () => {
        const formGroup = service.createCierreDeAccionFormGroup(sampleWithNewData);

        const cierreDeAccion = service.getCierreDeAccion(formGroup);

        expect(cierreDeAccion).toMatchObject(sampleWithNewData);
      });

      it('should return NewCierreDeAccion for empty CierreDeAccion initial value', () => {
        const formGroup = service.createCierreDeAccionFormGroup();

        const cierreDeAccion = service.getCierreDeAccion(formGroup);

        expect(cierreDeAccion).toMatchObject({});
      });

      it('should return ICierreDeAccion', () => {
        const formGroup = service.createCierreDeAccionFormGroup(sampleWithRequiredData);

        const cierreDeAccion = service.getCierreDeAccion(formGroup);

        expect(cierreDeAccion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICierreDeAccion should not enable id FormControl', () => {
        const formGroup = service.createCierreDeAccionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCierreDeAccion should disable id FormControl', () => {
        const formGroup = service.createCierreDeAccionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
