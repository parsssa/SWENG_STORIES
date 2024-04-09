import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreaStoriaComponent } from './crea-storia.component';

describe('CreaStoriaComponent', () => {
  let component: CreaStoriaComponent;
  let fixture: ComponentFixture<CreaStoriaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreaStoriaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreaStoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
