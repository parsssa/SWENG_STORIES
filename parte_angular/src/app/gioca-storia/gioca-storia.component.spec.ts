import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { GiocaStoriaComponent } from './gioca-storia.component';
import { StoryService } from '../story.service';
import { of } from 'rxjs';

describe('GiocaStorieComponent', () => {
  let component: GiocaStoriaComponent;
  let fixture: ComponentFixture<GiocaStoriaComponent>;
  let storyService: StoryService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GiocaStoriaComponent],
      imports: [
        HttpClientTestingModule,
        MatCardModule,
        MatButtonModule,
        MatFormFieldModule,
        MatInputModule,
        FormsModule,
        BrowserAnimationsModule
      ],
      providers: [StoryService]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GiocaStoriaComponent);
    component = fixture.componentInstance;
    storyService = TestBed.inject(StoryService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load story on init', () => {
    const storyMock = {
      startScenario: { title: 'Scenario 1', description: 'Descrizione dello scenario 1' }
    };
    spyOn(storyService, 'getStory').and.returnValue(of(storyMock));

    component.ngOnInit();

    expect(storyService.getStory).toHaveBeenCalledWith(1);
    expect(component.story).toEqual(storyMock);
    expect(component.currentScenario).toEqual(storyMock.startScenario);
  });

  it('should make a decision and update scenario', () => {
    const decisionMock = { text: 'Decisione 1' };
    const scenarioMock = {
      scenario: { title: 'Scenario 2', description: 'Descrizione dello scenario 2' },
      inventory: [{ name: 'Oggetto 1' }]
    };
    spyOn(storyService, 'makeDecision').and.returnValue(of(scenarioMock));

    component.makeDecision(decisionMock);

    expect(storyService.makeDecision).toHaveBeenCalledWith(1, decisionMock);
    expect(component.currentScenario).toEqual(scenarioMock.scenario);
    expect(component.inventory).toEqual(scenarioMock.inventory);
  });
});
