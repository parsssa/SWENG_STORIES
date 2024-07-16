import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

interface Story {
  id: number;
  title: string;
  firstScenario: string;
  scenarios: Scenario[];
}

interface Scenario {
  id: number;
  description: string;
  choices: Choice[];
}

interface Choice {
  text: string;
  nextScenarioId: number;
}

interface SavedGame {
  storyId: number;
  currentScenarioId: number;
  inventory: string[];
}

@Component({
  selector: 'app-gestione-scenario',
  templateUrl: './gestione-scenario.component.html',
  styleUrls: ['./gestione-scenario.component.scss']
})
export class GestioneScenarioComponent implements OnInit {
  stories: Story[] = [];
  savedGames: SavedGame[] = [];
  searchTerm: string = '';
  editingStory: Story | null = null;

  constructor(private dialog: MatDialog, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadStories();
    this.loadSavedGames();
  }

  loadStories(): void {
    this.stories = [
      { id: 1, title: 'Storia 1', firstScenario: 'Inizio della storia 1...', scenarios: [] },
      { id: 2, title: 'Storia 2', firstScenario: 'Inizio della storia 2...', scenarios: [] },
    ];
  }

  loadSavedGames(): void {
    this.savedGames = [
      { storyId: 1, currentScenarioId: 2, inventory: ['oggetto1', 'oggetto2'] },
    ];
  }

  searchStories(): Story[] {
    return this.stories.filter(story => 
      story.title.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  editStory(story: Story): void {
    this.editingStory = { ...story };
  }

  saveStoryChanges(): void {
    if (this.editingStory) {
      const index = this.stories.findIndex(s => s.id === this.editingStory!.id);
      if (index !== -1) {
        this.stories[index] = { ...this.editingStory };
        this.snackBar.open('Storia aggiornata con successo', 'Chiudi', { duration: 3000 });
      }
      this.editingStory = null;
    }
  }

  cancelEdit(): void {
    this.editingStory = null;
  }

  deleteSavedGame(savedGame: SavedGame): void {
    const index = this.savedGames.findIndex(sg => sg.storyId === savedGame.storyId);
    if (index !== -1) {
      this.savedGames.splice(index, 1);
      this.snackBar.open('Gioco salvato eliminato', 'Chiudi', { duration: 3000 });
    }
  }

  viewFirstScenario(story: Story): void {
    this.dialog.open(FirstScenarioDialogComponent, {
      data: { firstScenario: story.firstScenario }
    });
  }
}

@Component({
  selector: 'app-first-scenario-dialog',
  template: `
    
  `,
  styles: [`
    mat-dialog-content {
      max-height: 60vh;
      overflow-y: auto;
    }
    
    p {
      white-space: pre-wrap;
    }
  `]
})
export class FirstScenarioDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<FirstScenarioDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {firstScenario: string}
  ) {}
}