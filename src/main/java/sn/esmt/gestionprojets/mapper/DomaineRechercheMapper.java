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

        DomaineRechercheResponse response = new DomaineRechercheResponse();
        response.setId(domaine.getId());
        response.setNom(domaine.getNom());
        response.setCode(domaine.getCode());
        response.setDescription(domaine.getDescription());
        response.setNombreProjets(domaine.getProjets() != null ? domaine.getProjets().size() : 0);

        return response;
    }

    public List<DomaineRechercheResponse> toResponseList(List<DomaineRecherche> domaines) {
        return domaines.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DomaineRecherche toEntity(DomaineRechercheRequest request) {
        if (request == null) return null;

        DomaineRecherche domaine = new DomaineRecherche();
        domaine.setNom(request.getNom());
        domaine.setCode(request.getCode());
        domaine.setDescription(request.getDescription());

        return domaine;
    }

    public void updateEntityFromRequest(DomaineRecherche domaine, DomaineRechercheRequest request) {
        if (domaine == null || request == null) return;

        domaine.setNom(request.getNom());
        domaine.setCode(request.getCode());
        domaine.setDescription(request.getDescription());
    }
}