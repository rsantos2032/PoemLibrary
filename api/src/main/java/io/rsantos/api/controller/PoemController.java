package io.rsantos.api.controller;

import io.rsantos.api.entity.Poem;
import io.rsantos.api.service.PoemService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/poems")
@RequiredArgsConstructor
public class PoemController {
  private final PoemService poemService;

  @GetMapping("/id/{id}")
  public ResponseEntity<Poem> getPoemById(@PathVariable(value = "id") String id) {
    return ResponseEntity.ok().body(poemService.getPoemById(id));
  }

  @GetMapping("/title/{title}")
  public ResponseEntity<Poem> getPoemByTitle(@PathVariable(value = "title") String title) {
    return ResponseEntity.ok().body(poemService.getPoemByTitle(title));
  }

  @GetMapping
  public ResponseEntity<Page<Poem>> getPoems(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
    return ResponseEntity.ok().body(poemService.getAllPoems(page, size));
  }

  @PostMapping
  public ResponseEntity<Poem> createPoem(@RequestBody Poem poem) {
    Poem newPoem = poemService.createPoem(poem);
    URI location = URI.create("/poems/" + newPoem.getId());
    return ResponseEntity.created(location).body(newPoem);
  }

  @PutMapping("/id/{id}")
  public ResponseEntity<Poem> updatePoem(@PathVariable(value = "id") String id, @RequestBody Poem updatedPoem){
    Poem poem = poemService.updatePoem(id, updatedPoem);
    return ResponseEntity.ok().body(poemService.getPoemById(id));
  }

  @DeleteMapping("/id/{id}")
  public ResponseEntity<Void> deletePoem(@PathVariable(value = "id") String id) {
    poemService.deletePoem(id);
    return ResponseEntity.noContent().build();
  }
}
