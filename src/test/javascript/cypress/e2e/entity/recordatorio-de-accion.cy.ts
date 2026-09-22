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

describe('RecordatorioDeAccion e2e test', () => {
  const recordatorioDeAccionPageUrl = '/recordatorio-de-accion';
  let username: string;
  let password: string;
  // const recordatorioDeAccionSample = {"mensaje":"settle","nivelEscalamiento":1};

  let recordatorioDeAccion;
  // let asignacionDeAccion;

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
      url: '/api/asignacion-de-accions',
      body: {"titulo":"bashfully why","descripcion":"meal where briefly","fechaVencimiento":"2026-09-21","estado":"COMPLETADA","creadaEn":"2026-09-22T02:19:15.859Z"},
    }).then(({ body }) => {
      asignacionDeAccion = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/recordatorio-de-accions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/recordatorio-de-accions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/recordatorio-de-accions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/asignacion-de-accions', {
      statusCode: 200,
      body: [asignacionDeAccion],
    });

  });
   */

  afterEach(() => {
    if (recordatorioDeAccion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recordatorio-de-accions/${recordatorioDeAccion.id}`,
      }).then(() => {
        recordatorioDeAccion = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('RecordatorioDeAccions menu should load RecordatorioDeAccions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('recordatorio-de-accion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RecordatorioDeAccion').should('exist');
    cy.location('pathname').should('eq', recordatorioDeAccionPageUrl);
  });

  describe('RecordatorioDeAccion page', () => {
    it('should have translated page title', () => {
      cy.visit(recordatorioDeAccionPageUrl);
      cy.getEntityHeading('RecordatorioDeAccion').should('not.contain', 'oncallApp.recordatorioDeAccion.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(recordatorioDeAccionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RecordatorioDeAccion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${recordatorioDeAccionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('RecordatorioDeAccion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recordatorioDeAccionPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/recordatorio-de-accions',
          body: {
            ...recordatorioDeAccionSample,
            asignacion: asignacionDeAccion,
          },
        }).then(({ body }) => {
          recordatorioDeAccion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/recordatorio-de-accions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/recordatorio-de-accions?page=0&size=20>; rel="last",<http://localhost/api/recordatorio-de-accions?page=0&size=20>; rel="first"',
              },
              body: [recordatorioDeAccion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(recordatorioDeAccionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(recordatorioDeAccionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details RecordatorioDeAccion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('recordatorioDeAccion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recordatorioDeAccionPageUrl);
      });

      it('edit button click should load edit RecordatorioDeAccion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RecordatorioDeAccion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recordatorioDeAccionPageUrl);
      });

      it('edit button click should load edit RecordatorioDeAccion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RecordatorioDeAccion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recordatorioDeAccionPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of RecordatorioDeAccion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('recordatorioDeAccion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recordatorioDeAccionPageUrl);

        recordatorioDeAccion = undefined;
      });
    });
  });

  describe('new RecordatorioDeAccion page', () => {
    beforeEach(() => {
      cy.visit(recordatorioDeAccionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RecordatorioDeAccion');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of RecordatorioDeAccion', () => {
      cy.get(`[data-cy="mensaje"]`).type('basket traduce ouch');
      cy.get(`[data-cy="mensaje"]`).should('have.value', 'basket traduce ouch');

      cy.get(`[data-cy="enviadoEn"]`).type('2026-09-21T16:47');
      cy.get(`[data-cy="enviadoEn"]`).blur();
      cy.get(`[data-cy="enviadoEn"]`).should('have.value', '2026-09-21T16:47');

      cy.get(`[data-cy="nivelEscalamiento"]`).type('2');
      cy.get(`[data-cy="nivelEscalamiento"]`).should('have.value', '2');

      cy.get(`[data-cy="asignacion"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        recordatorioDeAccion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', recordatorioDeAccionPageUrl);
    });
  });
});
