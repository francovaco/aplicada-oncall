import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IRecordatorioDeAccion } from '../recordatorio-de-accion.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../recordatorio-de-accion.test-samples';

import { RecordatorioDeAccionService, RestRecordatorioDeAccion } from './recordatorio-de-accion.service';

const requireRestSample: RestRecordatorioDeAccion = {
  ...sampleWithRequiredData,
  enviadoEn: sampleWithRequiredData.enviadoEn?.toJSON(),
};

describe('RecordatorioDeAccion Service', () => {
  let service: RecordatorioDeAccionService;
  let httpMock: HttpTestingController;
  let expectedResult: IRecordatorioDeAccion | IRecordatorioDeAccion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RecordatorioDeAccionService);
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

    it('should create a RecordatorioDeAccion', () => {
      const recordatorioDeAccion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(recordatorioDeAccion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecordatorioDeAccion', () => {
      const recordatorioDeAccion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(recordatorioDeAccion).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RecordatorioDeAccion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RecordatorioDeAccion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RecordatorioDeAccion', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addRecordatorioDeAccionToCollectionIfMissing', () => {
      it('should add a RecordatorioDeAccion to an empty array', () => {
        const recordatorioDeAccion: IRecordatorioDeAccion = sampleWithRequiredData;
        expectedResult = service.addRecordatorioDeAccionToCollectionIfMissing([], recordatorioDeAccion);
        expect(expectedResult).toEqual([recordatorioDeAccion]);
      });

      it('should not add a RecordatorioDeAccion to an array that contains it', () => {
        const recordatorioDeAccion: IRecordatorioDeAccion = sampleWithRequiredData;
        const recordatorioDeAccionCollection: IRecordatorioDeAccion[] = [
          {
            ...recordatorioDeAccion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRecordatorioDeAccionToCollectionIfMissing(recordatorioDeAccionCollection, recordatorioDeAccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecordatorioDeAccion to an array that doesn't contain it", () => {
        const recordatorioDeAccion: IRecordatorioDeAccion = sampleWithRequiredData;
        const recordatorioDeAccionCollection: IRecordatorioDeAccion[] = [sampleWithPartialData];
        expectedResult = service.addRecordatorioDeAccionToCollectionIfMissing(recordatorioDeAccionCollection, recordatorioDeAccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recordatorioDeAccion);
      });

      it('should add only unique RecordatorioDeAccion to an array', () => {
        const recordatorioDeAccionArray: IRecordatorioDeAccion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const recordatorioDeAccionCollection: IRecordatorioDeAccion[] = [sampleWithRequiredData];
        expectedResult = service.addRecordatorioDeAccionToCollectionIfMissing(recordatorioDeAccionCollection, ...recordatorioDeAccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recordatorioDeAccion: IRecordatorioDeAccion = sampleWithRequiredData;
        const recordatorioDeAccion2: IRecordatorioDeAccion = sampleWithPartialData;
        expectedResult = service.addRecordatorioDeAccionToCollectionIfMissing([], recordatorioDeAccion, recordatorioDeAccion2);
        expect(expectedResult).toEqual([recordatorioDeAccion, recordatorioDeAccion2]);
      });

      it('should accept null and undefined values', () => {
        const recordatorioDeAccion: IRecordatorioDeAccion = sampleWithRequiredData;
        expectedResult = service.addRecordatorioDeAccionToCollectionIfMissing([], null, recordatorioDeAccion, undefined);
        expect(expectedResult).toEqual([recordatorioDeAccion]);
      });

      it('should return initial array if no RecordatorioDeAccion is added', () => {
        const recordatorioDeAccionCollection: IRecordatorioDeAccion[] = [sampleWithRequiredData];
        expectedResult = service.addRecordatorioDeAccionToCollectionIfMissing(recordatorioDeAccionCollection, undefined, null);
        expect(expectedResult).toEqual(recordatorioDeAccionCollection);
      });
    });

    describe('compareRecordatorioDeAccion', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRecordatorioDeAccion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30389 };
        const entity2 = null;

        const compareResult1 = service.compareRecordatorioDeAccion(entity1, entity2);
        const compareResult2 = service.compareRecordatorioDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30389 };
        const entity2 = { id: 14770 };

        const compareResult1 = service.compareRecordatorioDeAccion(entity1, entity2);
        const compareResult2 = service.compareRecordatorioDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 30389 };
        const entity2 = { id: 30389 };

        const compareResult1 = service.compareRecordatorioDeAccion(entity1, entity2);
        const compareResult2 = service.compareRecordatorioDeAccion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
