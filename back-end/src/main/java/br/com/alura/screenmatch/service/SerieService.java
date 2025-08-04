package br.com.alura.screenmatch.service;


import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repositorio;

    public List<SerieDTO> obterTodasAsSeries() {
        return converteDadosSerie(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDadosSerie(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDTO> converteDadosSerie(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(),
                        s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    private List<EpisodioDTO> converteDadosEpisodio(List<Episodio> episodio) {
        return episodio.stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumero(), e.getAvaliacao(),
                        e.getDataLancamento()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterTop5Lancamentos() {
//        return converteDados(repositorio.findTop5ByOrderByEpisodiosDataLancamentoDesc());
        return converteDadosSerie(repositorio.seriesMaisRecentes());
    }

    public SerieDTO obterPorId(long id) {
        Optional<Serie> serie = repositorio.findById(id);
        if (serie.isPresent()) {
            return new SerieDTO(serie.get().getId(), serie.get().getTitulo(), serie.get().getTotalTemporadas(),
                    serie.get().getAvaliacao(), serie.get().getGenero(), serie.get().getAtores(),
                    serie.get().getPoster(), serie.get().getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasAsTemporadas(long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            return converteDadosEpisodio(s.getEpisodios());
        }
        return null;
    }


//    public List<EpisodioDTO> obterTemporadasPorNumero(long id, int numero) {
//        Optional<Serie> serie = repositorio.findById(id);
//
//        if (serie.isPresent()) {
//            Serie s = serie.get();
//            return s.getEpisodios().stream()
//                    .filter(e -> e.getTemporada().equals(numero))
//                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumero(), e.getAvaliacao(),
//                            e.getDataLancamento()))
//                    .collect(Collectors.toList());
//
//        }
//        return Collections.emptyList();
//    }


    public List<EpisodioDTO> obterTemporadasPorNumero(long id, int numero) {
        return converteDadosEpisodio(repositorio.buscaEpisodiosPorTemporada(id, numero));
    }

    public List<SerieDTO> obterSeriesPorCategoria(String nomeGenero) {
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        return converteDadosSerie(repositorio.findByGenero(categoria));
    }
}
