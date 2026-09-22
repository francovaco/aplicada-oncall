import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICierreDeAccion, NewCierreDeAccion } from '../cierre-de-accion.model';

export type PartialUpdateCierreDeAccion = Partial<ICierreDeAccion> & Pick<ICierreDeAccion, 'id'>;

type RestOf<T extends ICierreDeAccion | NewCierreDeAccion> = Omit<T, 'cerradoEn'> & {
  cerradoEn?: string | null;
};

export type RestCierreDeAccion = RestOf<ICierreDeAccion>;

export type NewRestCierreDeAccion = RestOf<NewCierreDeAccion>;

export type PartialUpdateRestCierreDeAccion = RestOf<PartialUpdateCierreDeAccion>;

@Injectable()
export class CierreDeAccionsService {
  readonly cierreDeAccionsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly cierreDeAccionsResource = httpResource<RestCierreDeAccion[]>(() => {
    const params = this.cierreDeAccionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of cierreDeAccion that have been fetched. It is updated when the cierreDeAccionsResource emits a new value.
   * In case of error while fetching the cierreDeAccions, the signal is set to an empty array.
   */
  readonly cierreDeAccions = computed(() =>
    (this.cierreDeAccionsResource.hasValue() ? this.cierreDeAccionsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/cierre-de-accions');

  protected convertValueFromServer(restCierreDeAccion: RestCierreDeAccion): ICierreDeAccion {
    return {
      ...restCierreDeAccion,
      cerradoEn: restCierreDeAccion.cerradoEn ? dayjs(restCierreDeAccion.cerradoEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class CierreDeAccionService extends CierreDeAccionsService {
  protected readonly http = inject(HttpClient);

  create(cierreDeAccion: NewCierreDeAccion): Observable<ICierreDeAccion> {
    const copy = this.convertValueFromClient(cierreDeAccion);
    return this.http.post<RestCierreDeAccion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cierreDeAccion: ICierreDeAccion): Observable<ICierreDeAccion> {
    const copy = this.convertValueFromClient(cierreDeAccion);
    return this.http
      .put<RestCierreDeAccion>(`${this.resourceUrl}/${encodeURIComponent(this.getCierreDeAccionIdentifier(cierreDeAccion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cierreDeAccion: PartialUpdateCierreDeAccion): Observable<ICierreDeAccion> {
    const copy = this.convertValueFromClient(cierreDeAccion);
    return this.http
      .patch<RestCierreDeAccion>(`${this.resourceUrl}/${encodeURIComponent(this.getCierreDeAccionIdentifier(cierreDeAccion))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICierreDeAccion> {
    return this.http
      .get<RestCierreDeAccion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICierreDeAccion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCierreDeAccion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCierreDeAccionIdentifier(cierreDeAccion: Pick<ICierreDeAccion, 'id'>): number {
    return cierreDeAccion.id;
  }

  compareCierreDeAccion(o1: Pick<ICierreDeAccion, 'id'> | null, o2: Pick<ICierreDeAccion, 'id'> | null): boolean {
    return o1 && o2 ? this.getCierreDeAccionIdentifier(o1) === this.getCierreDeAccionIdentifier(o2) : o1 === o2;
  }

  addCierreDeAccionToCollectionIfMissing<Type extends Pick<ICierreDeAccion, 'id'>>(
    cierreDeAccionCollection: Type[],
    ...cierreDeAccionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cierreDeAccions: Type[] = cierreDeAccionsToCheck.filter(isPresent);
    if (cierreDeAccions.length > 0) {
      const cierreDeAccionCollectionIdentifiers = cierreDeAccionCollection.map(cierreDeAccionItem =>
        this.getCierreDeAccionIdentifier(cierreDeAccionItem),
      );
      const cierreDeAccionsToAdd = cierreDeAccions.filter(cierreDeAccionItem => {
        const cierreDeAccionIdentifier = this.getCierreDeAccionIdentifier(cierreDeAccionItem);
        if (cierreDeAccionCollectionIdentifiers.includes(cierreDeAccionIdentifier)) {
          return false;
        }
        cierreDeAccionCollectionIdentifiers.push(cierreDeAccionIdentifier);
        return true;
      });
      return [...cierreDeAccionsToAdd, ...cierreDeAccionCollection];
    }
    return cierreDeAccionCollection;
  }

  protected convertValueFromClient<T extends ICierreDeAccion | NewCierreDeAccion | PartialUpdateCierreDeAccion>(
    cierreDeAccion: T,
  ): RestOf<T> {
    return {
      ...cierreDeAccion,
      cerradoEn: cierreDeAccion.cerradoEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCierreDeAccion): ICierreDeAccion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCierreDeAccion[]): ICierreDeAccion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
