import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAsignacionDeAccion } from '../asignacion-de-accion.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../asignacion-de-accion.test-samples';

import { AsignacionDeAccionService, RestAsignacionDeAccion } from './asignacion-de-accion.service';

const requireRestSample: RestAsignacionDeAccion = {
  ...sampleWithRequiredData,
  fechaVencimiento: sampleWithRequiredData.fechaVencimiento?.format(DATE_FORMAT),
  creadaEn: sampleWithRequiredData.creadaEn?.toJSON(),
};

describe('AsignacionDeAccion Service', () => {
  let service: AsignacionDeAccionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAsignacionDeAccion | IAsignacionDeAccion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AsignacionDeAccionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AsignacionDeAccion', () => {
      const asignacionDeAccion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(asignacionDeAccion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AsignacionDeAccion', () => {
      const asignacionDeAccion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(asignacionDeAccion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AsignacionDeAccion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AsignacionDeAccion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AsignacionDeAccion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addAsignacionDeAccionToCollectionIfMissing', () => {
      it('should add a AsignacionDeAccion to an empty array', () => {
        const asignacionDeAccion: IAsignacionDeAccion = sampleWithRequiredData;
        expectedResult = service.addAsignacionDeAccionToCollectionIfMissing([], asignacionDeAccion);
        expect(expectedResult).toEqual([asignacionDeAccion]);
      });

      it('should not add a AsignacionDeAccion to an array that contains it', () => {
        const asignacionDeAccion: IAsignacionDeAccion = sampleWithRequiredData;
        const asignacionDeAccionCollection: IAsignacionDeAccion[] = [
          {
            ...asignacionDeAccion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAsignacionDeAccionToCollectionIfMissing(asignacionDeAccionCollection, asignacionDeAccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AsignacionDeAccion to an array that doesn't contain it", () => {
        const asignacionDeAccion: IAsignacionDeAccion = sampleWithRequiredData;
        const asignacionDeAccionCollection: IAsignacionDeAccion[] = [sampleWithPartialData];
        expectedResult = service.addAsignacionDeAccionToCollectionIfMissing(asignacionDeAccionCollection, asignacionDeAccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(asignacionDeAccion);
      });

      it('should add only unique AsignacionDeAccion to an array', () => {
        const asignacionDeAccionArray: IAsignacionDeAccion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const asignacionDeAccionCollection: IAsignacionDeAccion[] = [sampleWithRequiredData];
        expectedResult = service.addAsignacionDeAccionToCollectionIfMissing(asignacionDeAccionCollection, ...asignacionDeAccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const asignacionDeAccion: IAsignacionDeAccion = sampleWithRequiredData;
        const asignacionDeAccion2: IAsignacionDeAccion = sampleWithPartialData;
        expectedResult = service.addAsignacionDeAccionToCollectionIfMissing([], asignacionDeAccion, asignacionDeAccion2);
        expect(expectedResult).toEqual([asignacionDeAccion, asignacionDeAccion2]);
      });

      it('should accept null and undefined values', () => {
        const asignacionDeAccion: IAsignacionDeAccion = sampleWithRequiredData;
        expectedResult = service.addAsignacionDeAccionToCollectionIfMissing([], null, asignacionDeAccion, undefined);
        expect(expectedResult).toEqual([asignacionDeAccion]);
      });

      it('should return initial array if no AsignacionDeAccion is added', () => {
        const asignacionDeAccionCollection: IAsignacionDeAccion[] = [sampleWithRequiredData];
        expectedResult = service.addAsignacionDeAccionToCollectionIfMissing(asignacionDeAccionCollection, undefined, null);
        expect(expectedResult).toEqual(asignacionDeAccionCollection);
      });
    });

    describe('compareAsignacionDeAccion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAsignacionDeAccion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11301 };
        const entity2 = null;

        const compareResult1 = service.compareAsignacionDeAccion(entity1, entity2);
        const compareResult2 = service.compareAsignacionDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11301 };
        const entity2 = { id: 30988 };

        const compareResult1 = service.compareAsignacionDeAccion(entity1, entity2);
        const compareResult2 = service.compareAsignacionDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11301 };
        const entity2 = { id: 11301 };

        const compareResult1 = service.compareAsignacionDeAccion(entity1, entity2);
        const compareResult2 = service.compareAsignacionDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
