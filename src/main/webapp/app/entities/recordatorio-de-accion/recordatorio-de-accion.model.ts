import dayjs from 'dayjs/esm';

import { IAsignacionDeAccion } from 'app/entities/asignacion-de-accion/asignacion-de-accion.model';

export interface IRecordatorioDeAccion {
  id: number;
  mensaje?: string | null;
  enviadoEn?: dayjs.Dayjs | null;
  nivelEscalamiento?: number | null;
  asignacion?: Pick<IAsignacionDeAccion, 'id'> | null;
}

export type NewRecordatorioDeAccion = Omit<IRecordatorioDeAccion, 'id'> & { id: null };
