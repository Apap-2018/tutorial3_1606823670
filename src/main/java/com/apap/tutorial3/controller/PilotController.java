package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value = "id", required = true) String id,
					@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
					@RequestParam(value = "name", required = true) String name,
					@RequestParam(value = "flyHour", required = true) int flyHour, Model model) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		
		model.addAttribute("title", "add");
		model.addAttribute("action", "Ditambahkan");
		return "success";
	}
	
	@RequestMapping("pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}
	
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		
		model.addAttribute("listPilot", archive);
		return "viewall-pilot";
	}
	
	@RequestMapping(value = {"/pilot", "pilot/view/license-number/{licenseNumber}"})
	public String viewByLicenseNumber(@PathVariable Optional<String> licenseNumber, Model model) {		
		List<PilotModel> archive = pilotService.getPilotList();
		
		if(!licenseNumber.isPresent()) {
			model.addAttribute("msg", "Nomor Lisensi Tidak Terisi");
			return "fail";
						
		}else {
			for(PilotModel pilot : archive) {
				if(pilot.getLicenseNumber().equals(licenseNumber.get())) {
					model.addAttribute("pilot", pilot);
					return "view-pilot";
				}
			}
			model.addAttribute("msg", "Nomor Lisensi Tidak Ditemukan");
			return "fail";	
		}
	}
	
	@RequestMapping(value = {"/pilot", "pilot/update/license-number/{licenseNumber}/fly-hour/{newFlyHour}"})
	public String update(@PathVariable Optional<String> licenseNumber,
						@PathVariable Optional<Integer> newFlyHour, Model model) {	
		
		List<PilotModel> archive = pilotService.getPilotList();
		if(!licenseNumber.isPresent()) {
			model.addAttribute("msg", "Nomor Lisensi Tidak Terisi");
			return "fail";
						
		}else {
			for(PilotModel pilot : archive) {
				if(pilot.getLicenseNumber().equals(licenseNumber.get())) {
					pilot.setFlyHour(newFlyHour.get());
					model.addAttribute("action", "Diupdate");
					model.addAttribute("title", "Update");
					return "success";
				}
			}
			model.addAttribute("msg", "Nomor Lisensi Tidak Ditemukan");
			return "fail";	
		}
	}
	
	@RequestMapping(value = {"/pilot", "pilot/delete/id/{id}"})
	public String delete(@PathVariable Optional<String> id, Model model) {
		
		List<PilotModel> archive = pilotService.getPilotList();
		if(!id.isPresent()) {
			model.addAttribute("msg", "Nomor Id Tidak Terisi");
			return "fail";
						
		}else {
			for(PilotModel pilot : archive) {
				if(pilot.getId().equals(id.get())) {
					archive.remove(pilot);
					model.addAttribute("action", "Dihapus");
					model.addAttribute("title", "Delete");
					return "success";
				}
			}
			model.addAttribute("msg", "Nomor Id Tidak Ditemukan");
			return "fail";	
		}
	}
}
