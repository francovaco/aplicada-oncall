import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('AsignacionDeAccion e2e test', () => {
  const asignacionDeAccionPageUrl = '/asignacion-de-accion';
  let username: string;
  let password: string;
  // const asignacionDeAccionSample = {"titulo":"um","fechaVencimiento":"2026-09-21","estado":"EN_CURSO","creadaEn":"2026-09-21T09:20:32.918Z"};

  let asignacionDeAccion;
  // let accionCorrectiva;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/accion-correctivas',
      body: {"descripcion":"whoa inquisitively","prioridad":"BAJA","estado":"DESCARTADA","fechaLimite":"2023-12-04","ticketUrl":"incidentally because true"},
    }).then(({ body }) => {
      accionCorrectiva = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asignacion-de-accions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asignacion-de-accions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asignacion-de-accions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/accion-correctivas', {
      statusCode: 200,
      body: [accionCorrectiva],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (asignacionDeAccion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asignacion-de-accions/${asignacionDeAccion.id}`,
      }).then(() => {
        asignacionDeAccion = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (accionCorrectiva) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/accion-correctivas/${accionCorrectiva.id}`,
      }).then(() => {
        accionCorrectiva = undefined;
      });
    }
  });
   */

  it('AsignacionDeAccions menu should load AsignacionDeAccions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asignacion-de-accion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AsignacionDeAccion').should('exist');
    cy.location('pathname').should('eq', asignacionDeAccionPageUrl);
  });

  describe('AsignacionDeAccion page', () => {
    it('should have translated page title', () => {
      cy.visit(asignacionDeAccionPageUrl);
      cy.getEntityHeading('AsignacionDeAccion').should('not.contain', 'oncallApp.asignacionDeAccion.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(asignacionDeAccionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AsignacionDeAccion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${asignacionDeAccionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AsignacionDeAccion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', asignacionDeAccionPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asignacion-de-accions',
          body: {
            ...asignacionDeAccionSample,
            accionCorrectiva: accionCorrectiva,
          },
        }).then(({ body }) => {
          asignacionDeAccion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asignacion-de-accions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/asignacion-de-accions?page=0&size=20>; rel="last",<http://localhost/api/asignacion-de-accions?page=0&size=20>; rel="first"',
              },
              body: [asignacionDeAccion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(asignacionDeAccionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(asignacionDeAccionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AsignacionDeAccion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('asignacionDeAccion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', asignacionDeAccionPageUrl);
      });

      it('edit button click should load edit AsignacionDeAccion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AsignacionDeAccion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', asignacionDeAccionPageUrl);
      });

      it('edit button click should load edit AsignacionDeAccion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AsignacionDeAccion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', asignacionDeAccionPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of AsignacionDeAccion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('asignacionDeAccion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', asignacionDeAccionPageUrl);

        asignacionDeAccion = undefined;
      });
    });
  });

  describe('new AsignacionDeAccion page', () => {
    beforeEach(() => {
      cy.visit(asignacionDeAccionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AsignacionDeAccion');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of AsignacionDeAccion', () => {
      cy.get(`[data-cy="titulo"]`).type('frizzy');
      cy.get(`[data-cy="titulo"]`).should('have.value', 'frizzy');

      cy.get(`[data-cy="descripcion"]`).type('inasmuch');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'inasmuch');

      cy.get(`[data-cy="fechaVencimiento"]`).type('2026-09-21');
      cy.get(`[data-cy="fechaVencimiento"]`).blur();
      cy.get(`[data-cy="fechaVencimiento"]`).should('have.value', '2026-09-21');

      cy.get(`[data-cy="estado"]`).select('DESCARTADA');

      cy.get(`[data-cy="creadaEn"]`).type('2026-09-21T07:32');
      cy.get(`[data-cy="creadaEn"]`).blur();
      cy.get(`[data-cy="creadaEn"]`).should('have.value', '2026-09-21T07:32');

      cy.get(`[data-cy="accionCorrectiva"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        asignacionDeAccion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', asignacionDeAccionPageUrl);
    });
  });
});
