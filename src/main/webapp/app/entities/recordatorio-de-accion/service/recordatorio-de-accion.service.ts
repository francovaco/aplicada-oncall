import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IRecordatorioDeAccion, NewRecordatorioDeAccion } from '../recordatorio-de-accion.model';

export type PartialUpdateRecordatorioDeAccion = Partial<IRecordatorioDeAccion> & Pick<IRecordatorioDeAccion, 'id'>;

type RestOf<T extends IRecordatorioDeAccion | NewRecordatorioDeAccion> = Omit<T, 'enviadoEn'> & {
  enviadoEn?: string | null;
};

export type RestRecordatorioDeAccion = RestOf<IRecordatorioDeAccion>;

export type NewRestRecordatorioDeAccion = RestOf<NewRecordatorioDeAccion>;

export type PartialUpdateRestRecordatorioDeAccion = RestOf<PartialUpdateRecordatorioDeAccion>;

@Injectable()
export class RecordatorioDeAccionsService {
  readonly recordatorioDeAccionsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly recordatorioDeAccionsResource = httpResource<RestRecordatorioDeAccion[]>(() => {
    const params = this.recordatorioDeAccionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of recordatorioDeAccion that have been fetched. It is updated when the recordatorioDeAccionsResource emits a new value.
   * In case of error while fetching the recordatorioDeAccions, the signal is set to an empty array.
   */
  readonly recordatorioDeAccions = computed(() =>
    (this.recordatorioDeAccionsResource.hasValue() ? this.recordatorioDeAccionsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/recordatorio-de-accions');

  protected convertValueFromServer(restRecordatorioDeAccion: RestRecordatorioDeAccion): IRecordatorioDeAccion {
    return {
      ...restRecordatorioDeAccion,
      enviadoEn: restRecordatorioDeAccion.enviadoEn ? dayjs(restRecordatorioDeAccion.enviadoEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class RecordatorioDeAccionService extends RecordatorioDeAccionsService {
  protected readonly http = inject(HttpClient);

  create(recordatorioDeAccion: NewRecordatorioDeAccion): Observable<IRecordatorioDeAccion> {
    const copy = this.convertValueFromClient(recordatorioDeAccion);
    return this.http.post<RestRecordatorioDeAccion>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(recordatorioDeAccion: IRecordatorioDeAccion): Observable<IRecordatorioDeAccion> {
    const copy = this.convertValueFromClient(recordatorioDeAccion);
    return this.http
      .put<RestRecordatorioDeAccion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getRecordatorioDeAccionIdentifier(recordatorioDeAccion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(recordatorioDeAccion: PartialUpdateRecordatorioDeAccion): Observable<IRecordatorioDeAccion> {
    const copy = this.convertValueFromClient(recordatorioDeAccion);
    return this.http
      .patch<RestRecordatorioDeAccion>(
        `${this.resourceUrl}/${encodeURIComponent(this.getRecordatorioDeAccionIdentifier(recordatorioDeAccion))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IRecordatorioDeAccion> {
    return this.http
      .get<RestRecordatorioDeAccion>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IRecordatorioDeAccion[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRecordatorioDeAccion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRecordatorioDeAccionIdentifier(recordatorioDeAccion: Pick<IRecordatorioDeAccion, 'id'>): number {
    return recordatorioDeAccion.id;
  }

  compareRecordatorioDeAccion(o1: Pick<IRecordatorioDeAccion, 'id'> | null, o2: Pick<IRecordatorioDeAccion, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecordatorioDeAccionIdentifier(o1) === this.getRecordatorioDeAccionIdentifier(o2) : o1 === o2;
  }

  addRecordatorioDeAccionToCollectionIfMissing<Type extends Pick<IRecordatorioDeAccion, 'id'>>(
    recordatorioDeAccionCollection: Type[],
    ...recordatorioDeAccionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recordatorioDeAccions: Type[] = recordatorioDeAccionsToCheck.filter(isPresent);
    if (recordatorioDeAccions.length > 0) {
      const recordatorioDeAccionCollectionIdentifiers = recordatorioDeAccionCollection.map(recordatorioDeAccionItem =>
        this.getRecordatorioDeAccionIdentifier(recordatorioDeAccionItem),
      );
      const recordatorioDeAccionsToAdd = recordatorioDeAccions.filter(recordatorioDeAccionItem => {
        const recordatorioDeAccionIdentifier = this.getRecordatorioDeAccionIdentifier(recordatorioDeAccionItem);
        if (recordatorioDeAccionCollectionIdentifiers.includes(recordatorioDeAccionIdentifier)) {
          return false;
        }
        recordatorioDeAccionCollectionIdentifiers.push(recordatorioDeAccionIdentifier);
        return true;
      });
      return [...recordatorioDeAccionsToAdd, ...recordatorioDeAccionCollection];
    }
    return recordatorioDeAccionCollection;
  }

  protected convertValueFromClient<T extends IRecordatorioDeAccion | NewRecordatorioDeAccion | PartialUpdateRecordatorioDeAccion>(
    recordatorioDeAccion: T,
  ): RestOf<T> {
    return {
      ...recordatorioDeAccion,
      enviadoEn: recordatorioDeAccion.enviadoEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestRecordatorioDeAccion): IRecordatorioDeAccion {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestRecordatorioDeAccion[]): IRecordatorioDeAccion[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
