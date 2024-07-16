import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestioneScenarioComponent } from './gestione-scenario.component';

describe('GestioneScenarioComponent', () => {
  let component: GestioneScenarioComponent;
  let fixture: ComponentFixture<GestioneScenarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GestioneScenarioComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GestioneScenarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
