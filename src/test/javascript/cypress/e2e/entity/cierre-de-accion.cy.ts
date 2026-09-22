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

describe('CierreDeAccion e2e test', () => {
  const cierreDeAccionPageUrl = '/cierre-de-accion';
  let username: string;
  let password: string;
  // const cierreDeAccionSample = {"cerradoEn":"2026-09-21T03:26:37.589Z"};

  let cierreDeAccion;
  // let asignacionDeAccion;
  // let user;

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
      body: {"titulo":"unbearably","descripcion":"almost futon","fechaVencimiento":"2026-09-21","estado":"DESCARTADA","creadaEn":"2026-09-21T03:53:52.336Z"},
    }).then(({ body }) => {
      asignacionDeAccion = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"Barbara27","firstName":"Sancho","lastName":"Carranza Toledo","email":"Rosa.LaureanoHaro@yahoo.com","langKey":"little as ","imageUrl":"augment"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cierre-de-accions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cierre-de-accions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cierre-de-accions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/asignacion-de-accions', {
      statusCode: 200,
      body: [asignacionDeAccion],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (cierreDeAccion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cierre-de-accions/${cierreDeAccion.id}`,
      }).then(() => {
        cierreDeAccion = undefined;
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
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('CierreDeAccions menu should load CierreDeAccions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cierre-de-accion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CierreDeAccion').should('exist');
    cy.location('pathname').should('eq', cierreDeAccionPageUrl);
  });

  describe('CierreDeAccion page', () => {
    it('should have translated page title', () => {
      cy.visit(cierreDeAccionPageUrl);
      cy.getEntityHeading('CierreDeAccion').should('not.contain', 'oncallApp.cierreDeAccion.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cierreDeAccionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CierreDeAccion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cierreDeAccionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CierreDeAccion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cierreDeAccionPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cierre-de-accions',
          body: {
            ...cierreDeAccionSample,
            asignacion: asignacionDeAccion,
            cerradoPor: user,
          },
        }).then(({ body }) => {
          cierreDeAccion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cierre-de-accions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cierreDeAccion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cierreDeAccionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(cierreDeAccionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CierreDeAccion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cierreDeAccion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cierreDeAccionPageUrl);
      });

      it('edit button click should load edit CierreDeAccion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CierreDeAccion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cierreDeAccionPageUrl);
      });

      it('edit button click should load edit CierreDeAccion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CierreDeAccion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cierreDeAccionPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CierreDeAccion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cierreDeAccion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cierreDeAccionPageUrl);

        cierreDeAccion = undefined;
      });
    });
  });

  describe('new CierreDeAccion page', () => {
    beforeEach(() => {
      cy.visit(cierreDeAccionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CierreDeAccion');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CierreDeAccion', () => {
      cy.get(`[data-cy="comentarioCierre"]`).type('stable');
      cy.get(`[data-cy="comentarioCierre"]`).should('have.value', 'stable');

      cy.get(`[data-cy="cerradoEn"]`).type('2026-09-21T13:07');
      cy.get(`[data-cy="cerradoEn"]`).blur();
      cy.get(`[data-cy="cerradoEn"]`).should('have.value', '2026-09-21T13:07');

      cy.get(`[data-cy="asignacion"]`).select(1);
      cy.get(`[data-cy="cerradoPor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cierreDeAccion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cierreDeAccionPageUrl);
    });
  });
});
