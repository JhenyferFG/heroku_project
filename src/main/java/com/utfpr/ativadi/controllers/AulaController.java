package com.utfpr.ativadi.controllers;

import com.utfpr.ativadi.entities.AulaConcrete;
import com.utfpr.ativadi.entities.Mensagem;
import com.utfpr.ativadi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AulaController {
    private final AulaRepository aulaRepository;
    private final ProfessorRepository professorRepository;
    private final MateriaRepository materiaRepository;
    private final TurmaRepository turmaRepository;
    private final AtividadeRepository atividadeRepository;
    private final String ERROR = "errorMessage";
    private final String SUCESS = "sucessMessage";
    private final String INICIO = "index_aula";
    private final String TODAS_ATIVIDADES = "aulas";
    private final String LOAD_ATIVIDADES = "listaAtividades";
    private final String LOAD_PROFESSORES = "listaProfessores";
    private final String LOAD_MATERIAS = "listaMaterias";
    private final String LOAD_TURMAS = "listaTurmas";

    @Autowired
    public AulaController(AulaRepository aulaRepository, ProfessorRepository professorRepository,
                          MateriaRepository materiaRepository, TurmaRepository turmaRepository, AtividadeRepository atividadeRepository) {
        this.aulaRepository = aulaRepository;
        this.professorRepository = professorRepository;
        this.materiaRepository = materiaRepository;
        this.turmaRepository = turmaRepository;
        this.atividadeRepository = atividadeRepository;
    }

    @GetMapping("/aula")
    public String init(Model model) {
        model.addAttribute(TODAS_ATIVIDADES, aulaRepository.findAll());
        return INICIO;
    }

    @GetMapping("/newaula")
    public String abrirNovo(AulaConcrete aula, Model model) {
        model.addAttribute("aula", aula);
        model.addAttribute(LOAD_PROFESSORES, professorRepository.findAll());
        model.addAttribute(LOAD_MATERIAS, materiaRepository.findAll());
        model.addAttribute(LOAD_TURMAS, turmaRepository.findAll());
        model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());

        return "add_aula";
    }

    @PostMapping("/addaula")
    public String addAula(@Valid AulaConcrete aula, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(LOAD_PROFESSORES, professorRepository.findAll());
            model.addAttribute(LOAD_MATERIAS, materiaRepository.findAll());
            model.addAttribute(LOAD_TURMAS, turmaRepository.findAll());
            model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ADICIONAR).show());

            return "add_aula";
        }

        aulaRepository.save(aula);
        model.addAttribute(TODAS_ATIVIDADES, aulaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ADICIONAR).show());
        return INICIO;
    }

    @GetMapping("/editaula/{id}")
    public String abrirAtualizar(@PathVariable("id") long id, Model model) {
        AulaConcrete aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));
        model.addAttribute("aula", aula);
        model.addAttribute(LOAD_PROFESSORES, professorRepository.findAll());
        model.addAttribute(LOAD_MATERIAS, materiaRepository.findAll());
        model.addAttribute(LOAD_TURMAS, turmaRepository.findAll());
        model.addAttribute(LOAD_ATIVIDADES, atividadeRepository.findAll());

        return "update_aula";
    }

    @PostMapping("/updateaula/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid AulaConcrete aula, BindingResult result, Model model) {
        if (result.hasErrors()) {
            aula.setId(id);
            model.addAttribute(ERROR, Mensagem.getInstance(false, Mensagem.Funcao.ALTERAR).show());
            return "update_aula";
        }

        aulaRepository.save(aula);
        model.addAttribute(TODAS_ATIVIDADES, aulaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.ALTERAR).show());
        return INICIO;
    }

    @GetMapping("/deleteaula/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        AulaConcrete aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));
        aulaRepository.delete(aula);
        model.addAttribute(TODAS_ATIVIDADES, aulaRepository.findAll());
        model.addAttribute(SUCESS, Mensagem.getInstance(true, Mensagem.Funcao.REMOVER).show());
        return INICIO;
    }

    @GetMapping("/cloneaula/{id}")
    public String abrirClone(@PathVariable("id") long id, Model model) {
        AulaConcrete aula = aulaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id da Matéria inválido:" + id));

        AulaConcrete clone = (AulaConcrete) aula.clone();
        clone.setId(0);
        aulaRepository.save(clone);

        model.addAttribute(TODAS_ATIVIDADES, aulaRepository.findAll());

        return INICIO;
    }
}
