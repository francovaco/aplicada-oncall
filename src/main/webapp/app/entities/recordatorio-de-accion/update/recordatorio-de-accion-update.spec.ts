import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAsignacionDeAccion } from 'app/entities/asignacion-de-accion/asignacion-de-accion.model';
import { AsignacionDeAccionService } from 'app/entities/asignacion-de-accion/service/asignacion-de-accion.service';
import { IRecordatorioDeAccion } from '../recordatorio-de-accion.model';
import { RecordatorioDeAccionService } from '../service/recordatorio-de-accion.service';

import { RecordatorioDeAccionFormService } from './recordatorio-de-accion-form.service';
import { RecordatorioDeAccionUpdate } from './recordatorio-de-accion-update';

describe('RecordatorioDeAccion Management Update Component', () => {
  let comp: RecordatorioDeAccionUpdate;
  let fixture: ComponentFixture<RecordatorioDeAccionUpdate>;
  let activatedRoute: ActivatedRoute;
  let recordatorioDeAccionFormService: RecordatorioDeAccionFormService;
  let recordatorioDeAccionService: RecordatorioDeAccionService;
  let asignacionDeAccionService: AsignacionDeAccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(RecordatorioDeAccionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recordatorioDeAccionFormService = TestBed.inject(RecordatorioDeAccionFormService);
    recordatorioDeAccionService = TestBed.inject(RecordatorioDeAccionService);
    asignacionDeAccionService = TestBed.inject(AsignacionDeAccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AsignacionDeAccion query and add missing value', () => {
      const recordatorioDeAccion: IRecordatorioDeAccion = { id: 14770 };
      const asignacion: IAsignacionDeAccion = { id: 11301 };
      recordatorioDeAccion.asignacion = asignacion;

      const asignacionDeAccionCollection: IAsignacionDeAccion[] = [{ id: 11301 }];
      vitest.spyOn(asignacionDeAccionService, 'query').mockReturnValue(of(new HttpResponse({ body: asignacionDeAccionCollection })));
      const additionalAsignacionDeAccions = [asignacion];
      const expectedCollection: IAsignacionDeAccion[] = [...additionalAsignacionDeAccions, ...asignacionDeAccionCollection];
      vitest.spyOn(asignacionDeAccionService, 'addAsignacionDeAccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recordatorioDeAccion });
      comp.ngOnInit();

      expect(asignacionDeAccionService.query).toHaveBeenCalled();
      expect(asignacionDeAccionService.addAsignacionDeAccionToCollectionIfMissing).toHaveBeenCalledWith(
        asignacionDeAccionCollection,
        ...additionalAsignacionDeAccions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.asignacionDeAccionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const recordatorioDeAccion: IRecordatorioDeAccion = { id: 14770 };
      const asignacion: IAsignacionDeAccion = { id: 11301 };
      recordatorioDeAccion.asignacion = asignacion;

      activatedRoute.data = of({ recordatorioDeAccion });
      comp.ngOnInit();

      expect(comp.asignacionDeAccionsSharedCollection()).toContainEqual(asignacion);
      expect(comp.recordatorioDeAccion).toEqual(recordatorioDeAccion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRecordatorioDeAccion>();
      const recordatorioDeAccion = { id: 30389 };
      vitest.spyOn(recordatorioDeAccionFormService, 'getRecordatorioDeAccion').mockReturnValue(recordatorioDeAccion);
      vitest.spyOn(recordatorioDeAccionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recordatorioDeAccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(recordatorioDeAccion);
      saveSubject.complete();

      // THEN
      expect(recordatorioDeAccionFormService.getRecordatorioDeAccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recordatorioDeAccionService.update).toHaveBeenCalledWith(expect.objectContaining(recordatorioDeAccion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRecordatorioDeAccion>();
      const recordatorioDeAccion = { id: 30389 };
      vitest.spyOn(recordatorioDeAccionFormService, 'getRecordatorioDeAccion').mockReturnValue({ id: null });
      vitest.spyOn(recordatorioDeAccionService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recordatorioDeAccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(recordatorioDeAccion);
      saveSubject.complete();

      // THEN
      expect(recordatorioDeAccionFormService.getRecordatorioDeAccion).toHaveBeenCalled();
      expect(recordatorioDeAccionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IRecordatorioDeAccion>();
      const recordatorioDeAccion = { id: 30389 };
      vitest.spyOn(recordatorioDeAccionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recordatorioDeAccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recordatorioDeAccionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAsignacionDeAccion', () => {
      it('should forward to asignacionDeAccionService', () => {
        const entity = { id: 11301 };
        const entity2 = { id: 30988 };
        vitest.spyOn(asignacionDeAccionService, 'compareAsignacionDeAccion');
        comp.compareAsignacionDeAccion(entity, entity2);
        expect(asignacionDeAccionService.compareAsignacionDeAccion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
