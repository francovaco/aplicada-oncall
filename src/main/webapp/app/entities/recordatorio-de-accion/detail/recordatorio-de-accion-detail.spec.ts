import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { RecordatorioDeAccionDetail } from './recordatorio-de-accion-detail';

describe('RecordatorioDeAccion Management Detail Component', () => {
  let comp: RecordatorioDeAccionDetail;
  let fixture: ComponentFixture<RecordatorioDeAccionDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./recordatorio-de-accion-detail').then(m => m.RecordatorioDeAccionDetail),
              resolve: { recordatorioDeAccion: () => of({ id: 30389 }) },
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
    fixture = TestBed.createComponent(RecordatorioDeAccionDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load recordatorioDeAccion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RecordatorioDeAccionDetail);

      // THEN
      expect(instance.recordatorioDeAccion()).toEqual(expect.objectContaining({ id: 30389 }));
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
