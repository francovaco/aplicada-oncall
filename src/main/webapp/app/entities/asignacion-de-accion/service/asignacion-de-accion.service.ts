import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IAsignacionDeAccion, NewAsignacionDeAccion } from '../asignacion-de-accion.model';

export type PartialUpdateAsignacionDeAccion = Partial<IAsignacionDeAccion> & Pick<IAsignacionDeAccion, 'id'>;

type RestOf<T extends IAsignacionDeAccion | NewAsignacionDeAccion> = Omit<T, 'fechaVencimiento' | 'creadaEn'> & {
  fechaVencimiento?: string | null;
  creadaEn?: string | null;
};

export type RestAsignacionDeAccion = RestOf<IAsignacionDeAccion>;

export type NewRestAsignacionDeAccion = RestOf<NewAsignacionDeAccion>;

export type PartialUpdateRestAsignacionDeAccion = RestOf<PartialUpdateAsignacionDeAccion>;

@Injectable()
export class AsignacionDeAccionsService {
  readonly asignacionDeAccionsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly asignacionDeAccionsResource = httpResource<RestAsignacionDeAccion[]>(() => {
    const params = this.asignacionDeAccionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of asignacionDeAccion that have been fetched. It is updated when the asignacionDeAccionsResource emits a new value.
   * In case of error while fetching the asignacionDeAccions, the signal is set to an empty array.
   */
  readonly asignacionDeAccions = computed(() =>
    (this.asignacionDeAccionsResource.hasValue() ? this.asignacionDeAccionsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/asignacion-de-accions');

  protected convertValueFromServer(restAsignacionDeAccion: RestAsignacionDeAccion): IAsignacionDeAccion {
    return {
      ...restAsignacionDeAccion,
      fechaVencimiento: restAsignacionDeAccion.fechaVencimiento ? dayjs(restAsignacionDeAccion.fechaVencimiento) : undefined,
      creadaEn: restAsignacionDeAccion.creadaEn ? dayjs(restAsignacionDeAccion.creadaEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class AsignacionDeAccionService extends AsignacionDeAccionsService {
  protected readonly http = inject(HttpClient);

  create(asignacionDeAccion: NewAsignacionDeAccion): Observable<IAsignacionDeAccion> {
    const copy = this.convertValueFromClient(asignacionDeAccion);
    return this.http.post<RestAsignacionDeAccion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(asignacionDeAccion: IAsignacionDeAccion): Observable<IAsignacionDeAccion> {
    const copy = this.convertValueFromClient(asignacionDeAccion);
    return this.http
      .put<RestAsignacionDeAccion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getAsignacionDeAccionIdentifier(asignacionDeAccion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(asignacionDeAccion: PartialUpdateAsignacionDeAccion): Observable<IAsignacionDeAccion> {
    const copy = this.convertValueFromClient(asignacionDeAccion);
    return this.http
      .patch<RestAsignacionDeAccion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getAsignacionDeAccionIdentifier(asignacionDeAccion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IAsignacionDeAccion> {
    return this.http
      .get<RestAsignacionDeAccion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IAsignacionDeAccion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAsignacionDeAccion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAsignacionDeAccionIdentifier(asignacionDeAccion: Pick<IAsignacionDeAccion, 'id'>): number {
    return asignacionDeAccion.id;
  }

  compareAsignacionDeAccion(o1: Pick<IAsignacionDeAccion, 'id'> | null, o2: Pick<IAsignacionDeAccion, 'id'> | null): boolean {
    return o1 && o2 ? this.getAsignacionDeAccionIdentifier(o1) === this.getAsignacionDeAccionIdentifier(o2) : o1 === o2;
  }

  addAsignacionDeAccionToCollectionIfMissing<Type extends Pick<IAsignacionDeAccion, 'id'>>(
    asignacionDeAccionCollection: Type[],
    ...asignacionDeAccionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const asignacionDeAccions: Type[] = asignacionDeAccionsToCheck.filter(isPresent);
    if (asignacionDeAccions.length > 0) {
      const asignacionDeAccionCollectionIdentifiers = asignacionDeAccionCollection.map(asignacionDeAccionItem =>
        this.getAsignacionDeAccionIdentifier(asignacionDeAccionItem),
      );
      const asignacionDeAccionsToAdd = asignacionDeAccions.filter(asignacionDeAccionItem => {
        const asignacionDeAccionIdentifier = this.getAsignacionDeAccionIdentifier(asignacionDeAccionItem);
        if (asignacionDeAccionCollectionIdentifiers.includes(asignacionDeAccionIdentifier)) {
          return false;
        }
        asignacionDeAccionCollectionIdentifiers.push(asignacionDeAccionIdentifier);
        return true;
      });
      return [...asignacionDeAccionsToAdd, ...asignacionDeAccionCollection];
    }
    return asignacionDeAccionCollection;
  }

  protected convertValueFromClient<T extends IAsignacionDeAccion | NewAsignacionDeAccion | PartialUpdateAsignacionDeAccion>(
    asignacionDeAccion: T,
  ): RestOf<T> {
    return {
      ...asignacionDeAccion,
      fechaVencimiento: asignacionDeAccion.fechaVencimiento?.format(DATE_FORMAT) ?? null,
      creadaEn: asignacionDeAccion.creadaEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestAsignacionDeAccion): IAsignacionDeAccion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestAsignacionDeAccion[]): IAsignacionDeAccion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
