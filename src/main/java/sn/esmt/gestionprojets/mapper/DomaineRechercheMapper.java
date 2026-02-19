package sn.esmt.gestionprojets.mapper;

import org.springframework.stereotype.Component;
import sn.esmt.gestionprojets.dto.request.DomaineRechercheRequest;
import sn.esmt.gestionprojets.dto.response.DomaineRechercheResponse;
import sn.esmt.gestionprojets.entity.DomaineRecherche;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DomaineRechercheMapper {

    public DomaineRechercheResponse toResponse(DomaineRecherche domaine) {
        if (domaine == null) return null;

        return DomaineRechercheResponse.builder()
                .id(domaine.getId())
                .nom(domaine.getNom())
                .code(domaine.getCode())
                .description(domaine.getDescription())
                .nombreProjets(domaine.getProjets() != null ? domaine.getProjets().size() : 0)
                .build();
    }

    public List<DomaineRechercheResponse> toResponseList(List<DomaineRecherche> domaines) {
        return domaines.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DomaineRecherche toEntity(DomaineRechercheRequest request) {
        if (request == null) return null;

        return DomaineRecherche.builder()
                .nom(request.getNom())
                .code(request.getCode())
                .description(request.getDescription())
                .build();
    }

    public void updateEntityFromRequest(DomaineRecherche domaine, DomaineRechercheRequest request) {
        if (domaine == null || request == null) return;

        domaine.setNom(request.getNom());
        domaine.setCode(request.getCode());
        domaine.setDescription(request.getDescription());
    }
}
