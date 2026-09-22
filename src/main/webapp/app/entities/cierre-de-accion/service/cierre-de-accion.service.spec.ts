import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICierreDeAccion } from '../cierre-de-accion.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cierre-de-accion.test-samples';

import { CierreDeAccionService, RestCierreDeAccion } from './cierre-de-accion.service';

const requireRestSample: RestCierreDeAccion = {
  ...sampleWithRequiredData,
  cerradoEn: sampleWithRequiredData.cerradoEn?.toJSON(),
};

describe('CierreDeAccion Service', () => {
  let service: CierreDeAccionService;
  let httpMock: HttpTestingController;
  let expectedResult: ICierreDeAccion | ICierreDeAccion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CierreDeAccionService);
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

    it('should create a CierreDeAccion', () => {
      const cierreDeAccion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cierreDeAccion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CierreDeAccion', () => {
      const cierreDeAccion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cierreDeAccion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CierreDeAccion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CierreDeAccion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CierreDeAccion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCierreDeAccionToCollectionIfMissing', () => {
      it('should add a CierreDeAccion to an empty array', () => {
        const cierreDeAccion: ICierreDeAccion = sampleWithRequiredData;
        expectedResult = service.addCierreDeAccionToCollectionIfMissing([], cierreDeAccion);
        expect(expectedResult).toEqual([cierreDeAccion]);
      });

      it('should not add a CierreDeAccion to an array that contains it', () => {
        const cierreDeAccion: ICierreDeAccion = sampleWithRequiredData;
        const cierreDeAccionCollection: ICierreDeAccion[] = [
          {
            ...cierreDeAccion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCierreDeAccionToCollectionIfMissing(cierreDeAccionCollection, cierreDeAccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CierreDeAccion to an array that doesn't contain it", () => {
        const cierreDeAccion: ICierreDeAccion = sampleWithRequiredData;
        const cierreDeAccionCollection: ICierreDeAccion[] = [sampleWithPartialData];
        expectedResult = service.addCierreDeAccionToCollectionIfMissing(cierreDeAccionCollection, cierreDeAccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cierreDeAccion);
      });

      it('should add only unique CierreDeAccion to an array', () => {
        const cierreDeAccionArray: ICierreDeAccion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cierreDeAccionCollection: ICierreDeAccion[] = [sampleWithRequiredData];
        expectedResult = service.addCierreDeAccionToCollectionIfMissing(cierreDeAccionCollection, ...cierreDeAccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cierreDeAccion: ICierreDeAccion = sampleWithRequiredData;
        const cierreDeAccion2: ICierreDeAccion = sampleWithPartialData;
        expectedResult = service.addCierreDeAccionToCollectionIfMissing([], cierreDeAccion, cierreDeAccion2);
        expect(expectedResult).toEqual([cierreDeAccion, cierreDeAccion2]);
      });

      it('should accept null and undefined values', () => {
        const cierreDeAccion: ICierreDeAccion = sampleWithRequiredData;
        expectedResult = service.addCierreDeAccionToCollectionIfMissing([], null, cierreDeAccion, undefined);
        expect(expectedResult).toEqual([cierreDeAccion]);
      });

      it('should return initial array if no CierreDeAccion is added', () => {
        const cierreDeAccionCollection: ICierreDeAccion[] = [sampleWithRequiredData];
        expectedResult = service.addCierreDeAccionToCollectionIfMissing(cierreDeAccionCollection, undefined, null);
        expect(expectedResult).toEqual(cierreDeAccionCollection);
      });
    });

    describe('compareCierreDeAccion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCierreDeAccion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19963 };
        const entity2 = null;

        const compareResult1 = service.compareCierreDeAccion(entity1, entity2);
        const compareResult2 = service.compareCierreDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19963 };
        const entity2 = { id: 5686 };

        const compareResult1 = service.compareCierreDeAccion(entity1, entity2);
        const compareResult2 = service.compareCierreDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 19963 };
        const entity2 = { id: 19963 };

        const compareResult1 = service.compareCierreDeAccion(entity1, entity2);
        const compareResult2 = service.compareCierreDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
