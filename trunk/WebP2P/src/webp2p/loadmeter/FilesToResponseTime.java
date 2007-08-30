package webp2p.loadmeter;

/**
 * Tupla para representar os arquivos e seus respectivos tempos de respostas.
 * @author Jo�o Arthur Brunet Monteiro - jarthur@dsc.ufcg.edu.br
 */
public class FilesToResponseTime {
		
		private String file;
		private long responseTime;
		
		public FilesToResponseTime(String file, long responseTime) {
			this.file = file;
			this.responseTime = responseTime;
		}
		
		public String getFile() {
			return file;
		}
		
		public long getResponseTime() {
			return responseTime;
		}

				
		@Override
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((file == null) ? 0 : file.hashCode());
			result = PRIME * result + (int) (responseTime ^ (responseTime >>> 32));
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