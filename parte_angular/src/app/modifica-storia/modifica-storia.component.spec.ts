import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificaStoriaComponent } from './modifica-storia.component';

describe('ModificaStoriaComponent', () => {
  let component: ModificaStoriaComponent;
  let fixture: ComponentFixture<ModificaStoriaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModificaStoriaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModificaStoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
