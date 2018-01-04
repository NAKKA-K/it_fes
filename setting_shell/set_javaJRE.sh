sudo apt-get update
sudo apt-get install default-jdk

#setting JAVA_HOME
if [ ! -f /etc/profile.d/java.sh ]; then
	echo -n "JAVA_HOME=" > /etc/profile.d/java.sh
	sudo update-alternatives --list java | grep jdk | awk '{
		sub("/bin/.*", "");
		print $0;
	}' >> /etc/profile.d/java.sh
fi

source /etc/profile.d/java.sh
echo end setting $JAVA_HOME

