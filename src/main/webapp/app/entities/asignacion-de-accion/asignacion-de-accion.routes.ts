import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import AsignacionDeAccionResolve from './route/asignacion-de-accion-routing-resolve.service';

const asignacionDeAccionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/asignacion-de-accion').then(m => m.AsignacionDeAccion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/asignacion-de-accion-detail').then(m => m.AsignacionDeAccionDetail),
    resolve: {
      asignacionDeAccion: AsignacionDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/asignacion-de-accion-update').then(m => m.AsignacionDeAccionUpdate),
    resolve: {
      asignacionDeAccion: AsignacionDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/asignacion-de-accion-update').then(m => m.AsignacionDeAccionUpdate),
    resolve: {
      asignacionDeAccion: AsignacionDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default asignacionDeAccionRoute;
