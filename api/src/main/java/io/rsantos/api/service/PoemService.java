package io.rsantos.api.service;

import io.rsantos.api.entity.Poem;
import io.rsantos.api.repo.PoemRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class PoemService {
  private final PoemRepo poemRepo;

  public Poem getPoemById(String id) {
    return poemRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poem Not Found."));
  }

  public Poem getPoemByTitle(String title) {
    return poemRepo.findByTitle(title)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poem Not Found."));
  }

  public Page<Poem> getAllPoems(int page, int size) {
    return poemRepo.findAll(PageRequest.of(page, size, Sort.by("title")));
  }

  public Poem createPoem(Poem poem) {
    return poemRepo.save(poem);
  }

  public Poem updatePoem(String id, Poem updatedPoem) {
    Poem existingPoem = poemRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poem Not Found."));
    existingPoem.setTitle(updatedPoem.getTitle());
    existingPoem.setAuthor(updatedPoem.getAuthor());
    existingPoem.setPoem(updatedPoem.getPoem());
    return existingPoem;
  }

  public void deletePoem(String id) {
    Poem poem = poemRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poem Not Found."));
    poemRepo.delete(poem);
  }
}
