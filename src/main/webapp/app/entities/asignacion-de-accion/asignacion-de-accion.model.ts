import dayjs from 'dayjs/esm';

import { IAccionCorrectiva } from 'app/entities/accion-correctiva/accion-correctiva.model';
import { EstadoAccion } from 'app/entities/enumerations/estado-accion.model';
import { IUser } from 'app/entities/user/user.model';

export interface IAsignacionDeAccion {
  id: number;
  titulo?: string | null;
  descripcion?: string | null;
  fechaVencimiento?: dayjs.Dayjs | null;
  estado?: keyof typeof EstadoAccion | null;
  creadaEn?: dayjs.Dayjs | null;
  accionCorrectiva?: Pick<IAccionCorrectiva, 'id'> | null;
  responsable?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewAsignacionDeAccion = Omit<IAsignacionDeAccion, 'id'> & { id: null };
