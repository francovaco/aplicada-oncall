import dayjs from 'dayjs/esm';

import { ICierreDeAccion, NewCierreDeAccion } from './cierre-de-accion.model';

export const sampleWithRequiredData: ICierreDeAccion = {
  id: 6546,
  cerradoEn: dayjs('2026-09-22T01:39'),
};

export const sampleWithPartialData: ICierreDeAccion = {
  id: 2339,
  comentarioCierre: 'oh hm for',
  cerradoEn: dayjs('2026-09-21T07:33'),
};

export const sampleWithFullData: ICierreDeAccion = {
  id: 24390,
  comentarioCierre: 'now',
  cerradoEn: dayjs('2026-09-21T12:41'),
};

export const sampleWithNewData: NewCierreDeAccion = {
  cerradoEn: dayjs('2026-09-22T00:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
