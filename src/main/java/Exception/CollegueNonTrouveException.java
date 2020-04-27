package Exception;

import org.springframework.http.ResponseEntity;

public class CollegueNonTrouveException extends RuntimeException {
	public static ResponseEntity<Object> CollegueNonTrouveException() {
		return ResponseEntity.status(404).body("Collegue non trouve");
	}
}
