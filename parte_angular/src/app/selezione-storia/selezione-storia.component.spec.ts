import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelezioneStoriaComponent } from './selezione-storia.component';

describe('SelezioneStoriaComponent', () => {
  let component: SelezioneStoriaComponent;
  let fixture: ComponentFixture<SelezioneStoriaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SelezioneStoriaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SelezioneStoriaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
