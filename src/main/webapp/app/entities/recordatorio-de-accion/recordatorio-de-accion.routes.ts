import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import RecordatorioDeAccionResolve from './route/recordatorio-de-accion-routing-resolve.service';

const recordatorioDeAccionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/recordatorio-de-accion').then(m => m.RecordatorioDeAccion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/recordatorio-de-accion-detail').then(m => m.RecordatorioDeAccionDetail),
    resolve: {
      recordatorioDeAccion: RecordatorioDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/recordatorio-de-accion-update').then(m => m.RecordatorioDeAccionUpdate),
    resolve: {
      recordatorioDeAccion: RecordatorioDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/recordatorio-de-accion-update').then(m => m.RecordatorioDeAccionUpdate),
    resolve: {
      recordatorioDeAccion: RecordatorioDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default recordatorioDeAccionRoute;
