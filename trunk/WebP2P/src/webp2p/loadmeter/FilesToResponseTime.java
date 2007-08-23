package webp2p.loadmeter;

/**
 * Tupla para representar os arquivos e seus respectivos tempos de respostas.
 * @author João Arthur Brunet Monteiro - jarthur@dsc.ufcg.edu.br
 */
public class FilesToResponseTime {
		
		private String file;
		private int responseTime;
		
		public FilesToResponseTime(String file, int responseTime) {
			this.file = file;
			this.responseTime = responseTime;
		}
		
		public String getFile() {
			return file;
		}
		
		public int getResponseTime() {
			return responseTime;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((file == null) ? 0 : file.hashCode());
			result = prime * result + responseTime;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final FilesToResponseTime other = (FilesToResponseTime) obj;
			if (file == null) {
				if (other.file != null)
					return false;
			} else if (!file.equals(other.file))
				return false;
			if (responseTime != other.responseTime)
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return this.file + " " + this.responseTime;
		}
		
	
}