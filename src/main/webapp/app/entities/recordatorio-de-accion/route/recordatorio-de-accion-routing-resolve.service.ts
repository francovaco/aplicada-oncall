import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IRecordatorioDeAccion } from '../recordatorio-de-accion.model';
import { RecordatorioDeAccionService } from '../service/recordatorio-de-accion.service';

const recordatorioDeAccionResolve = (route: ActivatedRouteSnapshot): Observable<null | IRecordatorioDeAccion> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(RecordatorioDeAccionService);
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

export default recordatorioDeAccionResolve;
