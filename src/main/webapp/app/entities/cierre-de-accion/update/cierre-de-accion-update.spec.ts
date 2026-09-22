import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAsignacionDeAccion } from 'app/entities/asignacion-de-accion/asignacion-de-accion.model';
import { AsignacionDeAccionService } from 'app/entities/asignacion-de-accion/service/asignacion-de-accion.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { ICierreDeAccion } from '../cierre-de-accion.model';
import { CierreDeAccionService } from '../service/cierre-de-accion.service';

import { CierreDeAccionFormService } from './cierre-de-accion-form.service';
import { CierreDeAccionUpdate } from './cierre-de-accion-update';

describe('CierreDeAccion Management Update Component', () => {
  let comp: CierreDeAccionUpdate;
  let fixture: ComponentFixture<CierreDeAccionUpdate>;
  let activatedRoute: ActivatedRoute;
  let cierreDeAccionFormService: CierreDeAccionFormService;
  let cierreDeAccionService: CierreDeAccionService;
  let asignacionDeAccionService: AsignacionDeAccionService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(CierreDeAccionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cierreDeAccionFormService = TestBed.inject(CierreDeAccionFormService);
    cierreDeAccionService = TestBed.inject(CierreDeAccionService);
    asignacionDeAccionService = TestBed.inject(AsignacionDeAccionService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AsignacionDeAccion query and add missing value', () => {
      const cierreDeAccion: ICierreDeAccion = { id: 5686 };
      const asignacion: IAsignacionDeAccion = { id: 11301 };
      cierreDeAccion.asignacion = asignacion;

      const asignacionDeAccionCollection: IAsignacionDeAccion[] = [{ id: 11301 }];
      vitest.spyOn(asignacionDeAccionService, 'query').mockReturnValue(of(new HttpResponse({ body: asignacionDeAccionCollection })));
      const additionalAsignacionDeAccions = [asignacion];
      const expectedCollection: IAsignacionDeAccion[] = [...additionalAsignacionDeAccions, ...asignacionDeAccionCollection];
      vitest.spyOn(asignacionDeAccionService, 'addAsignacionDeAccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cierreDeAccion });
      comp.ngOnInit();

      expect(asignacionDeAccionService.query).toHaveBeenCalled();
      expect(asignacionDeAccionService.addAsignacionDeAccionToCollectionIfMissing).toHaveBeenCalledWith(
        asignacionDeAccionCollection,
        ...additionalAsignacionDeAccions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.asignacionDeAccionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const cierreDeAccion: ICierreDeAccion = { id: 5686 };
      const cerradoPor: IUser = { id: 3944 };
      cierreDeAccion.cerradoPor = cerradoPor;

      const userCollection: IUser[] = [{ id: 3944 }];
      vitest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [cerradoPor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vitest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cierreDeAccion });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cierreDeAccion: ICierreDeAccion = { id: 5686 };
      const asignacion: IAsignacionDeAccion = { id: 11301 };
      cierreDeAccion.asignacion = asignacion;
      const cerradoPor: IUser = { id: 3944 };
      cierreDeAccion.cerradoPor = cerradoPor;

      activatedRoute.data = of({ cierreDeAccion });
      comp.ngOnInit();

      expect(comp.asignacionDeAccionsSharedCollection()).toContainEqual(asignacion);
      expect(comp.usersSharedCollection()).toContainEqual(cerradoPor);
      expect(comp.cierreDeAccion).toEqual(cierreDeAccion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICierreDeAccion>();
      const cierreDeAccion = { id: 19963 };
      vitest.spyOn(cierreDeAccionFormService, 'getCierreDeAccion').mockReturnValue(cierreDeAccion);
      vitest.spyOn(cierreDeAccionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cierreDeAccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cierreDeAccion);
      saveSubject.complete();

      // THEN
      expect(cierreDeAccionFormService.getCierreDeAccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cierreDeAccionService.update).toHaveBeenCalledWith(expect.objectContaining(cierreDeAccion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICierreDeAccion>();
      const cierreDeAccion = { id: 19963 };
      vitest.spyOn(cierreDeAccionFormService, 'getCierreDeAccion').mockReturnValue({ id: null });
      vitest.spyOn(cierreDeAccionService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cierreDeAccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cierreDeAccion);
      saveSubject.complete();

      // THEN
      expect(cierreDeAccionFormService.getCierreDeAccion).toHaveBeenCalled();
      expect(cierreDeAccionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICierreDeAccion>();
      const cierreDeAccion = { id: 19963 };
      vitest.spyOn(cierreDeAccionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cierreDeAccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cierreDeAccionService.update).toHaveBeenCalled();
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

    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vitest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
