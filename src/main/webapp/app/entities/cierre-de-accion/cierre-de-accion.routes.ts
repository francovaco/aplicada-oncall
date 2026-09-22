import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CierreDeAccionResolve from './route/cierre-de-accion-routing-resolve.service';

const cierreDeAccionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cierre-de-accion').then(m => m.CierreDeAccion),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cierre-de-accion-detail').then(m => m.CierreDeAccionDetail),
    resolve: {
      cierreDeAccion: CierreDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cierre-de-accion-update').then(m => m.CierreDeAccionUpdate),
    resolve: {
      cierreDeAccion: CierreDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cierre-de-accion-update').then(m => m.CierreDeAccionUpdate),
    resolve: {
      cierreDeAccion: CierreDeAccionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cierreDeAccionRoute;
