import dayjs from 'dayjs/esm';

import { IAsignacionDeAccion, NewAsignacionDeAccion } from './asignacion-de-accion.model';

export const sampleWithRequiredData: IAsignacionDeAccion = {
  id: 28854,
  titulo: 'whenever happily overconfidently',
  fechaVencimiento: dayjs('2026-09-21'),
  estado: 'PENDIENTE',
  creadaEn: dayjs('2026-09-21T18:58'),
};

export const sampleWithPartialData: IAsignacionDeAccion = {
  id: 13914,
  titulo: 'besides',
  fechaVencimiento: dayjs('2026-09-21'),
  estado: 'PENDIENTE',
  creadaEn: dayjs('2026-09-21T10:14'),
};

export const sampleWithFullData: IAsignacionDeAccion = {
  id: 5224,
  titulo: 'aw near',
  descripcion: 'husky',
  fechaVencimiento: dayjs('2026-09-21'),
  estado: 'EN_CURSO',
  creadaEn: dayjs('2026-09-21T05:38'),
};

export const sampleWithNewData: NewAsignacionDeAccion = {
  titulo: 'annually',
  fechaVencimiento: dayjs('2026-09-21'),
  estado: 'DESCARTADA',
  creadaEn: dayjs('2026-09-21T18:21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
