import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAccionCorrectiva } from 'app/entities/accion-correctiva/accion-correctiva.model';
import { AccionCorrectivaService } from 'app/entities/accion-correctiva/service/accion-correctiva.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IAsignacionDeAccion } from '../asignacion-de-accion.model';
import { AsignacionDeAccionService } from '../service/asignacion-de-accion.service';

import { AsignacionDeAccionFormService } from './asignacion-de-accion-form.service';
import { AsignacionDeAccionUpdate } from './asignacion-de-accion-update';

describe('AsignacionDeAccion Management Update Component', () => {
  let comp: AsignacionDeAccionUpdate;
  let fixture: ComponentFixture<AsignacionDeAccionUpdate>;
  let activatedRoute: ActivatedRoute;
  let asignacionDeAccionFormService: AsignacionDeAccionFormService;
  let asignacionDeAccionService: AsignacionDeAccionService;
  let accionCorrectivaService: AccionCorrectivaService;
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

    fixture = TestBed.createComponent(AsignacionDeAccionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    asignacionDeAccionFormService = TestBed.inject(AsignacionDeAccionFormService);
    asignacionDeAccionService = TestBed.inject(AsignacionDeAccionService);
    accionCorrectivaService = TestBed.inject(AccionCorrectivaService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call AccionCorrectiva query and add missing value', () => {
      const asignacionDeAccion: IAsignacionDeAccion = { id: 30988 };
      const accionCorrectiva: IAccionCorrectiva = { id: 26689 };
      asignacionDeAccion.accionCorrectiva = accionCorrectiva;

      const accionCorrectivaCollection: IAccionCorrectiva[] = [{ id: 26689 }];
      vitest.spyOn(accionCorrectivaService, 'query').mockReturnValue(of(new HttpResponse({ body: accionCorrectivaCollection })));
      const additionalAccionCorrectivas = [accionCorrectiva];
      const expectedCollection: IAccionCorrectiva[] = [...additionalAccionCorrectivas, ...accionCorrectivaCollection];
      vitest.spyOn(accionCorrectivaService, 'addAccionCorrectivaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ asignacionDeAccion });
      comp.ngOnInit();

      expect(accionCorrectivaService.query).toHaveBeenCalled();
      expect(accionCorrectivaService.addAccionCorrectivaToCollectionIfMissing).toHaveBeenCalledWith(
        accionCorrectivaCollection,
        ...additionalAccionCorrectivas.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.accionCorrectivasSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const asignacionDeAccion: IAsignacionDeAccion = { id: 30988 };
      const responsable: IUser = { id: 3944 };
      asignacionDeAccion.responsable = responsable;

      const userCollection: IUser[] = [{ id: 3944 }];
      vitest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [responsable];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vitest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ asignacionDeAccion });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const asignacionDeAccion: IAsignacionDeAccion = { id: 30988 };
      const accionCorrectiva: IAccionCorrectiva = { id: 26689 };
      asignacionDeAccion.accionCorrectiva = accionCorrectiva;
      const responsable: IUser = { id: 3944 };
      asignacionDeAccion.responsable = responsable;

      activatedRoute.data = of({ asignacionDeAccion });
      comp.ngOnInit();

      expect(comp.accionCorrectivasSharedCollection()).toContainEqual(accionCorrectiva);
      expect(comp.usersSharedCollection()).toContainEqual(responsable);
      expect(comp.asignacionDeAccion).toEqual(asignacionDeAccion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAsignacionDeAccion>();
      const asignacionDeAccion = { id: 11301 };
      vitest.spyOn(asignacionDeAccionFormService, 'getAsignacionDeAccion').mockReturnValue(asignacionDeAccion);
      vitest.spyOn(asignacionDeAccionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asignacionDeAccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(asignacionDeAccion);
      saveSubject.complete();

      // THEN
      expect(asignacionDeAccionFormService.getAsignacionDeAccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(asignacionDeAccionService.update).toHaveBeenCalledWith(expect.objectContaining(asignacionDeAccion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAsignacionDeAccion>();
      const asignacionDeAccion = { id: 11301 };
      vitest.spyOn(asignacionDeAccionFormService, 'getAsignacionDeAccion').mockReturnValue({ id: null });
      vitest.spyOn(asignacionDeAccionService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asignacionDeAccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(asignacionDeAccion);
      saveSubject.complete();

      // THEN
      expect(asignacionDeAccionFormService.getAsignacionDeAccion).toHaveBeenCalled();
      expect(asignacionDeAccionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAsignacionDeAccion>();
      const asignacionDeAccion = { id: 11301 };
      vitest.spyOn(asignacionDeAccionService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ asignacionDeAccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(asignacionDeAccionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAccionCorrectiva', () => {
      it('should forward to accionCorrectivaService', () => {
        const entity = { id: 26689 };
        const entity2 = { id: 12026 };
        vitest.spyOn(accionCorrectivaService, 'compareAccionCorrectiva');
        comp.compareAccionCorrectiva(entity, entity2);
        expect(accionCorrectivaService.compareAccionCorrectiva).toHaveBeenCalledWith(entity, entity2);
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
