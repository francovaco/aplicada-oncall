import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICierreDeAccion } from '../cierre-de-accion.model';
import { CierreDeAccionService } from '../service/cierre-de-accion.service';

const cierreDeAccionResolve = (route: ActivatedRouteSnapshot): Observable<null | ICierreDeAccion> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CierreDeAccionService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default cierreDeAccionResolve;
