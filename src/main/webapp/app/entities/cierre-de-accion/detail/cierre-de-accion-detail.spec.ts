import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CierreDeAccionDetail } from './cierre-de-accion-detail';

describe('CierreDeAccion Management Detail Component', () => {
  let comp: CierreDeAccionDetail;
  let fixture: ComponentFixture<CierreDeAccionDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cierre-de-accion-detail').then(m => m.CierreDeAccionDetail),
              resolve: { cierreDeAccion: () => of({ id: 19963 }) },
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
    fixture = TestBed.createComponent(CierreDeAccionDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load cierreDeAccion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CierreDeAccionDetail);

      // THEN
      expect(instance.cierreDeAccion()).toEqual(expect.objectContaining({ id: 19963 }));
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
