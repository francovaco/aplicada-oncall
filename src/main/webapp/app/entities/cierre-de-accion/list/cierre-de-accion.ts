import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { TranslatePipe } from '@ngx-translate/core';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ICierreDeAccion } from '../cierre-de-accion.model';
import { CierreDeAccionDeleteDialog } from '../delete/cierre-de-accion-delete-dialog';
import { CierreDeAccionService } from '../service/cierre-de-accion.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-cierre-de-accion',
  templateUrl: './cierre-de-accion.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    TranslatePipe,
    FormatMediumDatetimePipe,
  ],
})
export class CierreDeAccion implements OnInit {
  subscription: Subscription | null = null;
  readonly cierreDeAccions = signal<ICierreDeAccion[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly cierreDeAccionService = inject(CierreDeAccionService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.cierreDeAccionService.cierreDeAccionsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.cierreDeAccions.set(this.fillComponentAttributesFromResponseBody([...this.cierreDeAccionService.cierreDeAccions()]));
    });
  }

  trackId = (item: ICierreDeAccion): number => this.cierreDeAccionService.getCierreDeAccionIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.cierreDeAccions().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(cierreDeAccion: ICierreDeAccion): void {
    const modalRef = this.modalService.open(CierreDeAccionDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cierreDeAccion = cierreDeAccion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: ICierreDeAccion[]): ICierreDeAccion[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: ICierreDeAccion[]): ICierreDeAccion[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.cierreDeAccionService.cierreDeAccionsParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
