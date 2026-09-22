import dayjs from 'dayjs/esm';

import { IRecordatorioDeAccion, NewRecordatorioDeAccion } from './recordatorio-de-accion.model';

export const sampleWithRequiredData: IRecordatorioDeAccion = {
  id: 26756,
  mensaje: 'gah impossible',
  nivelEscalamiento: 2,
};

export const sampleWithPartialData: IRecordatorioDeAccion = {
  id: 6950,
  mensaje: 'fully rule athwart',
  nivelEscalamiento: 2,
};

export const sampleWithFullData: IRecordatorioDeAccion = {
  id: 10737,
  mensaje: 'unrealistic caring soulful',
  enviadoEn: dayjs('2026-09-21T10:04'),
  nivelEscalamiento: 1,
};

export const sampleWithNewData: NewRecordatorioDeAccion = {
  mensaje: 'plain searchingly',
  nivelEscalamiento: 1,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
