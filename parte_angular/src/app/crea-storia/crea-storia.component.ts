import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-crea-storia',
  templateUrl: './crea-storia.component.html',
  styleUrls: ['./crea-storia.component.scss']
})
export class CreaStoriaComponent {
  storyForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.storyForm = this.fb.group({
      title: [''],
      description: [''],
      start: [''],
      alternatives: this.fb.array([
        this.fb.control('')
      ]),
      riddle: [''],
      inventory: ['']
    });
  }

  get alternatives() {
    return this.storyForm.get('alternatives') as FormArray;
  }

  addAlternative() {
    this.alternatives.push(this.fb.control(''));
  }

  resetForm() {
    this.storyForm.reset();
    while (this.alternatives.length !== 0) {
      this.alternatives.removeAt(0);
    }
    this.addAlternative();
  }

  onSubmit() {
    if (this.storyForm.valid) {
      console.log('Form Submitted!', this.storyForm.value);
      // Aggiungi qui la logica per gestire l'invio del modulo, ad esempio una chiamata API
    }
  }
}
