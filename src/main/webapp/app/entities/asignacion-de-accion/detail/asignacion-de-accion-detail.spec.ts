import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { AsignacionDeAccionDetail } from './asignacion-de-accion-detail';

describe('AsignacionDeAccion Management Detail Component', () => {
  let comp: AsignacionDeAccionDetail;
  let fixture: ComponentFixture<AsignacionDeAccionDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./asignacion-de-accion-detail').then(m => m.AsignacionDeAccionDetail),
              resolve: { asignacionDeAccion: () => of({ id: 11301 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AsignacionDeAccionDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load asignacionDeAccion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AsignacionDeAccionDetail);

      // THEN
      expect(instance.asignacionDeAccion()).toEqual(expect.objectContaining({ id: 11301 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
