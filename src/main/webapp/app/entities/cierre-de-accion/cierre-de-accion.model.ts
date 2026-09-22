import dayjs from 'dayjs/esm';

import { IAsignacionDeAccion } from 'app/entities/asignacion-de-accion/asignacion-de-accion.model';
import { IUser } from 'app/entities/user/user.model';

export interface ICierreDeAccion {
  id: number;
  comentarioCierre?: string | null;
  cerradoEn?: dayjs.Dayjs | null;
  asignacion?: Pick<IAsignacionDeAccion, 'id'> | null;
  cerradoPor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewCierreDeAccion = Omit<ICierreDeAccion, 'id'> & { id: null };
