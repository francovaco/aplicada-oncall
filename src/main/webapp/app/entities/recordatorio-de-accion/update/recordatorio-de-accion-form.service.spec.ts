import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../recordatorio-de-accion.test-samples';

import { RecordatorioDeAccionFormService } from './recordatorio-de-accion-form.service';

describe('RecordatorioDeAccion Form Service', () => {
  let service: RecordatorioDeAccionFormService;

  beforeEach(() => {
    service = TestBed.inject(RecordatorioDeAccionFormService);
  });

  describe('Service methods', () => {
    describe('createRecordatorioDeAccionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecordatorioDeAccionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mensaje: expect.any(Object),
            enviadoEn: expect.any(Object),
            nivelEscalamiento: expect.any(Object),
            asignacion: expect.any(Object),
          }),
        );
      });

      it('passing IRecordatorioDeAccion should create a new form with FormGroup', () => {
        const formGroup = service.createRecordatorioDeAccionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mensaje: expect.any(Object),
            enviadoEn: expect.any(Object),
            nivelEscalamiento: expect.any(Object),
            asignacion: expect.any(Object),
          }),
        );
      });
    });

    describe('getRecordatorioDeAccion', () => {
      it('should return NewRecordatorioDeAccion for default RecordatorioDeAccion initial value', () => {
        const formGroup = service.createRecordatorioDeAccionFormGroup(sampleWithNewData);

        const recordatorioDeAccion = service.getRecordatorioDeAccion(formGroup);

        expect(recordatorioDeAccion).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecordatorioDeAccion for empty RecordatorioDeAccion initial value', () => {
        const formGroup = service.createRecordatorioDeAccionFormGroup();

        const recordatorioDeAccion = service.getRecordatorioDeAccion(formGroup);

        expect(recordatorioDeAccion).toMatchObject({});
      });

      it('should return IRecordatorioDeAccion', () => {
        const formGroup = service.createRecordatorioDeAccionFormGroup(sampleWithRequiredData);

        const recordatorioDeAccion = service.getRecordatorioDeAccion(formGroup);

        expect(recordatorioDeAccion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecordatorioDeAccion should not enable id FormControl', () => {
        const formGroup = service.createRecordatorioDeAccionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecordatorioDeAccion should disable id FormControl', () => {
        const formGroup = service.createRecordatorioDeAccionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
