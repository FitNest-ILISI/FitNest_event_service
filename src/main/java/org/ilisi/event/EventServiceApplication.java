package org.ilisi.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableJpaRepositories
public class EventServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
	}


/*
	@Bean
	CommandLineRunner cmd(SportCategoryRepository sportCategoryRepository, EventRepository eventRepository) {
		return args -> {
			// Create and save sample SportCategory
			SportCategory football = SportCategory.builder()
					.id(1L)
					.name("Football")
					.build();
			sportCategoryRepository.save(football);

			SportCategory basketball = SportCategory.builder()
					.id(2L).name("Basketball")
					.build();
			sportCategoryRepository.save(basketball);

			// Create and save sample Event
			Event footballEvent = Event.builder()
					.idCoordinator(1L)
					.name("Football Match")
					.description("A friendly football match")
					.maxParticipants(22)
					.currentNumParticipants(10)
					.sportCategory(football) // Link to the 'Football' category
					.build();

			eventRepository.save(footballEvent);

			Event basketballEvent = Event.builder()
					.idCoordinator(2L)
					.name("Basketball Tournament")
					.description("A competitive basketball tournament")
					.maxParticipants(10)
					.currentNumParticipants(8)
					.sportCategory(basketball) // Link to the 'Basketball' category
					.build();

			eventRepository.save(basketballEvent);

			// Log the events
			System.out.println("Sample events have been saved!");
		};
	}
*/
}
